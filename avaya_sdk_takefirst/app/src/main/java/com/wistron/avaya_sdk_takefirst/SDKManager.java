package com.wistron.avaya_sdk_takefirst;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

import com.avaya.clientservices.call.AllowedVideoDirection;
import com.avaya.clientservices.call.Call;
import com.avaya.clientservices.call.CallEndReason;
import com.avaya.clientservices.call.CallException;
import com.avaya.clientservices.call.CallListener;
import com.avaya.clientservices.call.CallPrecedenceLevel;
import com.avaya.clientservices.call.CallPreemptionReason;
import com.avaya.clientservices.call.CallService;
import com.avaya.clientservices.call.CallServiceListener;
import com.avaya.clientservices.call.VideoChannel;
import com.avaya.clientservices.call.VideoMode;
import com.avaya.clientservices.client.Client;
import com.avaya.clientservices.client.ClientConfiguration;
import com.avaya.clientservices.client.ClientListener;
import com.avaya.clientservices.client.CreateUserCompletionHandler;
import com.avaya.clientservices.client.UserCreatedException;
import com.avaya.clientservices.collaboration.WCSConfiguration;
import com.avaya.clientservices.common.ConnectionPolicy;
import com.avaya.clientservices.common.MessageBodyPart;
import com.avaya.clientservices.common.ServerInfo;
import com.avaya.clientservices.common.SignalingServer;
import com.avaya.clientservices.credentials.Challenge;
import com.avaya.clientservices.credentials.CredentialCompletionHandler;
import com.avaya.clientservices.credentials.CredentialProvider;
import com.avaya.clientservices.media.VoIPConfigurationAudio;
import com.avaya.clientservices.media.VoIPConfigurationVideo;
import com.avaya.clientservices.presence.PresenceConfiguration;
import com.avaya.clientservices.provider.amm.AMMConfiguration;
import com.avaya.clientservices.provider.media.MediaConfiguration;
import com.avaya.clientservices.provider.ppm.PPMConfiguration;
import com.avaya.clientservices.provider.sip.MobilityMode;
import com.avaya.clientservices.provider.sip.SIPUserConfiguration;
import com.avaya.clientservices.user.LocalContactConfiguration;
import com.avaya.clientservices.user.User;
import com.avaya.clientservices.user.UserConfiguration;
import com.avaya.clientservices.user.UserRegistrationListener;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SDKManager implements UserRegistrationListener, ClientListener, CredentialProvider, CallServiceListener, CallListener {
    private static final String LOG_TAG = SDKManager.class.getSimpleName();

    private static volatile SDKManager instance;
    private final Activity activity;
    private Client mClient;
    private UserConfiguration userConfiguration;
    private boolean isIPOEnabled = false;
    private User mUser;

    private SDKManager(Activity activity) {
        this.activity = activity;
        //callsMap = new SparseArray<>();
    }

    public static SDKManager getInstance(Activity activity) {
        if (instance == null) {
            synchronized (SDKManager.class) {
                if (instance == null) {
                    instance = new SDKManager(activity);
                }
            }
        }
        return instance;
    }
    // Configure and create mClient
    public void setupClientConfiguration(Application application) {

        Resources resources = activity.getResources();
        // Create client configuration

        String dataDirectory = "data";// = resources.getString("data");
        ClientConfiguration clientConfiguration = new ClientConfiguration(application.getApplicationContext(), dataDirectory);

        // A unique instance id of the user agent defined in RFC 5626.
        // For the real applications please generate unique value once (e.g. UUID [RFC4122]) and
        // then save this in persistent storage for all future use.
        clientConfiguration.setUserAgentInstanceId(GenerateUserAgentInstanceId(application));

        // Set media configuration
        final MediaConfiguration mediaConfiguration = new MediaConfiguration();
        mediaConfiguration.setVoIPConfigurationAudio(new VoIPConfigurationAudio());
        mediaConfiguration.setVoIPConfigurationVideo(new VoIPConfigurationVideo());
        clientConfiguration.setMediaConfiguration(mediaConfiguration);
        // Create Client
        mClient = new Client(clientConfiguration, application, this);
        mClient.setLogLevel(Client.LogLevel.DEBUG);
    }

    // Configure and create mUser
    public void setupUserConfiguration() {
        // Initialize shared preferences
        // Getting SIP configuration details from settings
        String address = "aurademo.avaya.com.tw";
        int port = 5601;
        String domain = "avaya.com.tw";
        boolean useTls = true;
        String extension = "501032";

        // Create SIP configuration
        userConfiguration = new UserConfiguration();
        SIPUserConfiguration sipConfig = userConfiguration.getSIPUserConfiguration();

        // Set SIP service enabled and configure userID and domain
        sipConfig.setEnabled(true);
        sipConfig.setUserId(extension);
        sipConfig.setDomain(domain);

        // Configure Session Manager connection details
        SignalingServer.TransportType transportType =
                useTls ? SignalingServer.TransportType.TLS : SignalingServer.TransportType.TCP;
        SignalingServer sipSignalingServer = new SignalingServer(transportType, address, port,
                SignalingServer.FailbackPolicy.AUTOMATIC);
        sipConfig.setConnectionPolicy(new ConnectionPolicy(sipSignalingServer));

        // Set CredentialProvider
        sipConfig.setCredentialProvider(this);

        //For the case when application doesn't have READ_PHONE_STATE permission
        // to avoid calling getDeviceCellularNumber call end throwing SecurityException

        sipConfig.setMobilityMode(MobilityMode.MOBILE);

        // Configuring local contacts
        boolean localContactsPermission = checkLocalContactsPermission();

        LocalContactConfiguration localContactConfiguration = new LocalContactConfiguration();
        localContactConfiguration.setEnabled(localContactsPermission);
        userConfiguration.setLocalContactConfiguration(localContactConfiguration);


        userConfiguration.setLocalCallLogFilePath(Environment.getExternalStorageDirectory().getPath()
                + File.separatorChar + "call_logs_sampleApps.xml");

        // There are some deployment specific configuration handled in below methods,
        // change the below boolean value according to your deployment.
        if (isIPOEnabled) {
            //setupIPOSpecificConfiguration();
        } else {
            setupAuraSpecificConfiguration();
        }

        setupWCS();

        // Finally create and login a user
        register();
    }

    private void setupWCS() {
        WCSConfiguration wcsConfiguration = new WCSConfiguration();
        wcsConfiguration.setEnabled(true);
        userConfiguration.setWCSConfiguration(wcsConfiguration);
    }


    private void register() {
        Log.d(LOG_TAG, "Register user");
        if (mUser != null) {
            // Login if user already exist
            mUser.start();
        } else {
            // Create user if not created yet
            mClient.createUser(userConfiguration, new CreateUserCompletionHandler() {
                @Override
                public void onSuccess(User user) {
                    Log.d(LOG_TAG, "createUser onSuccess");
                    // Initialize class member mUser if we created user successfully
                    mUser = user;
                    Log.d(LOG_TAG, "User Id = " + mUser.getUserId());
                    mUser.addRegistrationListener(SDKManager.this);

                    CallService callService = mUser.getCallService();
                    if (callService != null) {
                        Log.d(LOG_TAG, "CallService is ready to use");
                        // Subscribe to CallService events for incoming call handling
                        callService.addListener(getInstance(activity));
                    }
                    // Initialize class member MessagingManager if we created user successfully and AMM parameters is configured.
                    //if (isAMMEnabled) {
                    //    messagingManager = new MessagingManager(mUser.getMessagingService(), activity);
                    //}
                    // And login
                    mUser.start();
                }

                @Override
                public void onError(UserCreatedException e) {
                    Log.e(LOG_TAG, "createUser onError " + e.getFailureReason());

                    //Send broadcast to notify BaseActivity to show message to the user
                   // activity.sendBroadcast(new Intent(MESSAGE_RECEIVER).putExtra(TOAST_TAG,
                   //         "ERROR: " + e.getFailureReason().toString()));
                }
            });
        }
    }

    private void setupAuraSpecificConfiguration() {
        // Configure PPM service for Send All calls feature. PPM service can be stand alone server
        // as well as part of Session Manager. In the code below SM server details will be used to
        // access PPM. Use ppmConfiguration.setServerInfo() to configure stand alone PPM server
        PPMConfiguration ppmConfiguration = userConfiguration.getPPMConfiguration();
        ppmConfiguration.setEnabled(true);

        // Using SIP credential provider. You can use SIP credential provider for PPM if you have
        // same login details. Please create another credential provider in case PPM authentication
        // details are different from SIP user configuration details.
        ppmConfiguration.setCredentialProvider(this);

        // Configure presence service to get presence for contacts
        PresenceConfiguration presenceConfiguration = userConfiguration.getPresenceConfiguration();
        presenceConfiguration.setEnabled(true);

        /*
        Configure AMM service. Creating the instance of AMMConfiguration for provide amm settings to CSDK.
        Should contain the separate CredentialProvider for messaging, instance of the ServerInfo, amm server IP,
        port for amm connection.
        */
       /* AMMConfiguration ammConfiguration = new AMMConfiguration();

        String ammServerAddress = settings.getString(SDKManager.AMM_ADDRESS, "");
        int ammPort = settings.getInt(SDKManager.AMM_PORT, 8443);
        int pollIntervalInMinutes = settings.getInt(SDKManager.AMM_REFRESH, 0);
        if (!ammServerAddress.isEmpty()) {
            isAMMEnabled = true;
        }
        ammConfiguration.setEnabled(isAMMEnabled);
        boolean useAmmTls = true;
        ServerInfo serverInfo = new ServerInfo(ammServerAddress, ammPort, useAmmTls);
        ammConfiguration.setServerInfo(serverInfo);
        ammConfiguration.setPollIntervalInMinutes(pollIntervalInMinutes);
        MessagingCredentialProvider messagingCredentialProvider = new MessagingCredentialProvider(settings);
        ammConfiguration.setCredentialProvider(messagingCredentialProvider);
        userConfiguration.setAMMConfiguration(ammConfiguration);*/
    }

    private boolean checkLocalContactsPermission(){
        PackageManager packageManager = activity.getPackageManager();
        int readPermission = packageManager.checkPermission(Manifest.permission.READ_CONTACTS, activity.getPackageName());
        int writePermission = packageManager.checkPermission(Manifest.permission.WRITE_CONTACTS, activity.getPackageName());
        return (readPermission == PackageManager.PERMISSION_GRANTED) && (writePermission == PackageManager.PERMISSION_GRANTED);
    }

    // Generates unique UserAgentInstanceId value
    private static String GenerateUserAgentInstanceId(Context context) {
        String androidID = android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        if (androidID.length() > 0) {
            //deterministic generation (based on ANDROID_ID)
            return UUID.nameUUIDFromBytes(androidID.getBytes()).toString();
        } else {
            //random generation (if ANDROID_ID isn't available)
            return UUID.randomUUID().toString();
        }
    }

    public Call createCall(String calledParty) {
        // Create call
        CallService callService = mUser.getCallService();
        Call call = callService.createCall();
        // Set far-end's number
        call.setRemoteAddress(calledParty);
        // Get unique call id specified for created call
        //int callId = call.getCallId();

        //CallWrapper callWrapper = new CallWrapper(call);

        // Add the call to call Map
        //callsMap.put(callId, callWrapper);
        return call;
    }


    public void startCall(Call c) {

        Call call = c;
        // Subscribe to call state events
        call.addListener(this);

        // Add video to the call
        //if (callWrapper.isVideoCall()) {
        //    addVideo(call);
        //}

        if (call.isIncoming()) {
            Log.d(LOG_TAG, "Incoming call accepted");
            // Accept the call if it is incoming call
            call.accept();
        } else {
            Log.d(LOG_TAG, "Outgoing call started");
            // Start the call if it is outgoing call
            call.start();
        }
    }

    @Override
    public void onClientShutdown(Client client) {
        Log.d(LOG_TAG, "onClientShutdown");
    }

    @Override
    public void onClientUserCreated(Client client, User user) {
        Log.d(LOG_TAG, "onClientUserCreated");
    }

    @Override
    public void onClientUserRemoved(Client client, User user) {
        Log.d(LOG_TAG, "onClientUserRemoved");
    }

    @Override
    public void onIdentityCertificateEnrollmentFailed(Client client, int i, String s, String s1) {
        Log.d(LOG_TAG, "onIdentityCertificateEnrollmentFailed");
    }

    @Override
    public void onAuthenticationChallenge(Challenge challenge, CredentialCompletionHandler credentialCompletionHandler) {

    }

    @Override
    public void onCredentialAccepted(Challenge challenge) {

    }

    @Override
    public void onAuthenticationChallengeCancelled(Challenge challenge) {

    }

    @Override
    public boolean supportsPreEmptiveChallenge() {
        return false;
    }

    @Override
    public void onUserRegistrationInProgress(User user, SignalingServer signalingServer) {

    }

    @Override
    public void onUserRegistrationSuccessful(User user, SignalingServer signalingServer) {

    }

    @Override
    public void onUserRegistrationFailed(User user, SignalingServer signalingServer, Exception e) {

    }

    @Override
    public void onUserAllRegistrationsSuccessful(User user) {

    }

    @Override
    public void onUserAllRegistrationsFailed(User user, boolean b) {

    }

    @Override
    public void onUserUnregistrationInProgress(User user, SignalingServer signalingServer) {

    }

    @Override
    public void onUserUnregistrationSuccessful(User user, SignalingServer signalingServer) {

    }

    @Override
    public void onUserUnregistrationFailed(User user, SignalingServer signalingServer, Exception e) {

    }

    @Override
    public void onUserUnregistrationComplete(User user) {

    }

    @Override
    public void onRegistrationResponsePayloadReceived(User user, List<MessageBodyPart> list, SignalingServer signalingServer) {

    }

    @Override
    public void onCallStarted(Call call) {

    }

    @Override
    public void onCallRemoteAlerting(Call call, boolean b) {

    }

    @Override
    public void onCallRedirected(Call call) {

    }

    @Override
    public void onCallQueued(Call call) {

    }

    @Override
    public void onCallEstablished(Call call) {

    }

    @Override
    public void onCallRemoteAddressChanged(Call call, String s, String s1) {

    }

    @Override
    public void onCallHeld(Call call) {

    }

    @Override
    public void onCallUnheld(Call call) {

    }

    @Override
    public void onCallHeldRemotely(Call call) {

    }

    @Override
    public void onCallUnheldRemotely(Call call) {

    }

    @Override
    public void onCallJoined(Call call) {

    }

    @Override
    public void onCallEnded(Call call, CallEndReason callEndReason) {

    }

    @Override
    public void onCallFailed(Call call, CallException e) {

    }

    @Override
    public void onCallDenied(Call call) {

    }

    @Override
    public void onCallIgnored(Call call) {

    }

    @Override
    public void onCallAudioMuteStatusChanged(Call call, boolean b) {

    }

    @Override
    public void onCallSpeakerSilenceStatusChanged(Call call, boolean b) {

    }

    @Override
    public void onCallVideoChannelsUpdated(Call call, List<VideoChannel> list) {

    }

    @Override
    public void onCallIncomingVideoAddRequestReceived(Call call) {

    }

    @Override
    public void onCallIncomingVideoAddRequestAccepted(Call call, VideoChannel videoChannel) {

    }

    @Override
    public void onCallIncomingVideoAddRequestDenied(Call call) {

    }

    @Override
    public void onCallIncomingVideoAddRequestTimedOut(Call call) {

    }

    @Override
    public void onCallConferenceStatusChanged(Call call, boolean b) {

    }

    @Override
    public void onCallCapabilitiesChanged(Call call) {

    }

    @Override
    public void onCallServiceAvailable(Call call) {

    }

    @Override
    public void onCallServiceUnavailable(Call call) {

    }

    @Override
    public void onCallParticipantMatchedContactsChanged(Call call) {

    }

    @Override
    public void onCallDigitCollectionPlayDialTone(Call call) {

    }

    @Override
    public void onCallDigitCollectionCompleted(Call call) {

    }

    @Override
    public void onCallPrecedenceLevelChanged(Call call, CallPrecedenceLevel callPrecedenceLevel) {

    }

    @Override
    public void onCallPreempted(Call call, CallPreemptionReason callPreemptionReason, boolean b) {

    }

    @Override
    public void onCallAllowedVideoDirectionChanged(Call call, AllowedVideoDirection allowedVideoDirection) {

    }

    @Override
    public void onCallExtraPropertiesChanged(Call call, Map<String, String> map) {

    }

    @Override
    public void onIncomingCallReceived(CallService callService, Call call) {

    }

    @Override
    public void onCallCreated(CallService callService, Call call) {

    }

    @Override
    public void onIncomingCallUndelivered(CallService callService, Call call) {

    }

    @Override
    public void onCallRemoved(CallService callService, Call call) {

    }

    @Override
    public void onCallServiceCapabilityChanged(CallService callService) {

    }

    @Override
    public void onActiveCallChanged(CallService callService, Call call) {

    }

    @Override
    public void onStartCallRequestReceived(CallService callService, Call call, VideoMode videoMode) {

    }

    @Override
    public void onAcceptCallRequestReceived(CallService callService, Call call, VideoMode videoMode) {

    }
}
