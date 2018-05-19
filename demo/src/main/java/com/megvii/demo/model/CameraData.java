package com.megvii.demo.model;

import java.io.Serializable;
import java.util.List;

public class CameraData implements Serializable {

    /**
     * addParams : [{"name":"DeviceUrl","value":"http://192.168.1.231:8899/onvif/device_service"},{"name":"MaxFPS","value":"25"},{"name":"VideoLayout","value":""},{"name":"bitrateInfos","value":"{\"streams\":[{\"actualBitrate\":3.1703240871429443,\"actualFps\":25,\"averageGopSize\":5.3025122765956256e-17,\"bitrateFactor\":1,\"bitratePerGop\":\"BPG_None\",\"encoderIndex\":0,\"fps\":25,\"isConfigured\":true,\"numberOfChannels\":1,\"rawSuggestedBitrate\":3.1911900043487549,\"resolution\":\"1920x1080\",\"suggestedBitrate\":3.1904296875,\"timestamp\":\"2018-05-17T08:36:11Z\"},{\"actualBitrate\":0.27866363525390625,\"actualFps\":8.5714282989501953,\"averageGopSize\":0.036545254290103912,\"bitrateFactor\":1,\"bitratePerGop\":\"BPG_None\",\"encoderIndex\":1,\"fps\":7,\"isConfigured\":true,\"numberOfChannels\":1,\"rawSuggestedBitrate\":0.1875,\"resolution\":\"352x288\",\"suggestedBitrate\":0.1875,\"timestamp\":\"2018-05-17T08:36:42Z\"}]}"},{"name":"cameraAdvancedParams","value":"{\"groups\":[{\"description\":\"\",\"groups\":[{\"description\":\"\",\"groups\":[],\"name\":\"Backlight Compensation\",\"params\":[{\"dataType\":\"Enumeration\",\"dependencies\":[],\"description\":\"Enable or disable backlight compensation on the device.\",\"id\":\"ibcMode\",\"internalRange\":\"\",\"name\":\"Mode\",\"range\":\"Off,On\",\"readCmd\":\"\",\"readOnly\":false,\"tag\":\"\",\"writeCmd\":\"\"}]}],\"name\":\"Imaging\",\"params\":[{\"dataType\":\"Number\",\"dependencies\":[],\"description\":\"Image brightness.\",\"id\":\"iBri\",\"internalRange\":\"\",\"name\":\"Brightness\",\"range\":\"0,100\",\"readCmd\":\"\",\"readOnly\":false,\"tag\":\"\",\"writeCmd\":\"\"},{\"dataType\":\"Number\",\"dependencies\":[],\"description\":\"Color saturation of the image.\",\"id\":\"iCS\",\"internalRange\":\"\",\"name\":\"Color Saturation\",\"range\":\"0,100\",\"readCmd\":\"\",\"readOnly\":false,\"tag\":\"\",\"writeCmd\":\"\"},{\"dataType\":\"Number\",\"dependencies\":[],\"description\":\"Contrast of the image.\",\"id\":\"iCon\",\"internalRange\":\"\",\"name\":\"Contrast\",\"range\":\"0,100\",\"readCmd\":\"\",\"readOnly\":false,\"tag\":\"\",\"writeCmd\":\"\"},{\"dataType\":\"Number\",\"dependencies\":[],\"description\":\"Sharpness of the Video image.\",\"id\":\"iSha\",\"internalRange\":\"\",\"name\":\"Sharpness\",\"range\":\"0,15\",\"readCmd\":\"\",\"readOnly\":false,\"tag\":\"\",\"writeCmd\":\"\"}]},{\"description\":\"\",\"groups\":[],\"name\":\"Maintenance\",\"params\":[{\"dataType\":\"Button\",\"dependencies\":[],\"description\":\"This operation reboots the device.\",\"id\":\"mReboot\",\"internalRange\":\"\",\"name\":\"System Reboot\",\"range\":\"\",\"readCmd\":\"\",\"readOnly\":false,\"tag\":\"\",\"writeCmd\":\"\"},{\"dataType\":\"Button\",\"dependencies\":[],\"description\":\"This operation reloads all parameters on the device to their factory default values, except basic network settings like IP address, subnet and gateway or DHCP settings.\",\"id\":\"mSoftReset\",\"internalRange\":\"\",\"name\":\"Soft Factory Reset\",\"range\":\"\",\"readCmd\":\"\",\"readOnly\":false,\"tag\":\"\",\"writeCmd\":\"\"},{\"dataType\":\"Button\",\"dependencies\":[],\"description\":\"This operation reloads all parameters on the device to their factory default values.\",\"id\":\"mHardReset\",\"internalRange\":\"\",\"name\":\"Hard Factory Reset\",\"range\":\"\",\"readCmd\":\"\",\"readOnly\":false,\"tag\":\"\",\"writeCmd\":\"\"}]}],\"name\":\"ONVIF\",\"packet_mode\":true,\"unique_id\":\"53148996-d4d8-4e90-985e-24c6c63c5150\",\"version\":\"1\"}"},{"name":"defaultCredentials","value":"1f44e379b0973a45d2e60cba0c585ca7"},{"name":"firmware","value":"V4.02.R11.D5302532.10012.242800..ONVIF 2.41"},{"name":"hasDualStreaming","value":"1"},{"name":"ioSettings","value":"[]"},{"name":"isAudioSupported","value":"1"},{"name":"mediaStreams","value":"{\"streams\":[{\"codec\":28,\"customStreamParams\":{\"profile-level-id\":\"4d0014\",\"sprop-parameter-sets\":\"Z00AFJWoWCWhAAAAAQAAAA6E,aO48gA==\"},\"encoderIndex\":1,\"resolution\":\"352x288\",\"transcodingRequired\":false,\"transports\":[\"rtsp\",\"hls\"]},{\"codec\":28,\"customStreamParams\":{\"profile-level-id\":\"4d002a\",\"sprop-parameter-sets\":\"Z00AKpWoHgCJ+WEAAAABAAAAMoQ=,aO48gA==\"},\"encoderIndex\":0,\"resolution\":\"1920x1080\",\"transcodingRequired\":false,\"transports\":[\"rtsp\",\"hls\"]},{\"codec\":0,\"customStreamParams\":{},\"encoderIndex\":-1,\"resolution\":\"*\",\"transcodingRequired\":true,\"transports\":[\"rtsp\",\"mjpeg\",\"webm\"]}]}"},{"name":"ptzCapabilities","value":"7"},{"name":"streamUrls","value":"{\n    \"1\": \"rtsp://192.168.1.231:554/user=admin_password=nTBCS19C_channel=1_stream=0.sdp?real_stream\",\n    \"2\": \"rtsp://192.168.1.231:554/user=admin_password=nTBCS19C_channel=1_stream=1.sdp?real_stream\"\n}\n"}]
     * audioEnabled : false
     * backupType : CameraBackupDisabled
     * controlEnabled : false
     * dewarpingParams :
     * failoverPriority : Never
     * groupId :
     * groupName :
     * id : {02cf3f47-9e2b-f6ec-4b37-19afb1768ad5}
     * licenseUsed : false
     * mac : 00-12-17-61-F2-B6
     * manuallyAdded : false
     * maxArchiveDays : -30
     * minArchiveDays : -1
     * model : NVT
     * motionMask :
     * motionType : MT_Default
     * name : IPC-modelNVT
     * parentId : {c6ecdac9-b4ed-8a45-9aeb-09d2e9e90e83}
     * physicalId : 00-12-17-61-F2-B6
     * preferredServerId : {00000000-0000-0000-0000-000000000000}
     * scheduleEnabled : false
     * scheduleTasks : []
     * secondaryStreamQuality : SSQualityLow
     * status : Online
     * statusFlags : CSF_NoFlags
     * typeId : {9a55ee6b-a595-5807-a5ba-d4aff697dc12}
     * url : http://192.168.1.231:8899/onvif/device_service
     * userDefinedGroupName :
     * vendor : IPC-model
     */

    private boolean audioEnabled;
    private String backupType;
    private boolean controlEnabled;
    private String dewarpingParams;
    private String failoverPriority;
    private String groupId;
    private String groupName;
    private String id;
    private boolean licenseUsed;
    private String mac;
    private boolean manuallyAdded;
    private int maxArchiveDays;
    private int minArchiveDays;
    private String model;
    private String motionMask;
    private String motionType;
    private String name;
    private String parentId;
    private String physicalId;
    private String preferredServerId;
    private boolean scheduleEnabled;
    private String secondaryStreamQuality;
    private String status;
    private String statusFlags;
    private String typeId;
    private String url;
    private String userDefinedGroupName;
    private String vendor;
    private List<AddParamsBean> addParams;
    private List<?> scheduleTasks;

    public boolean isAudioEnabled() {
        return audioEnabled;
    }

    public void setAudioEnabled(boolean audioEnabled) {
        this.audioEnabled = audioEnabled;
    }

    public String getBackupType() {
        return backupType;
    }

    public void setBackupType(String backupType) {
        this.backupType = backupType;
    }

    public boolean isControlEnabled() {
        return controlEnabled;
    }

    public void setControlEnabled(boolean controlEnabled) {
        this.controlEnabled = controlEnabled;
    }

    public String getDewarpingParams() {
        return dewarpingParams;
    }

    public void setDewarpingParams(String dewarpingParams) {
        this.dewarpingParams = dewarpingParams;
    }

    public String getFailoverPriority() {
        return failoverPriority;
    }

    public void setFailoverPriority(String failoverPriority) {
        this.failoverPriority = failoverPriority;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLicenseUsed() {
        return licenseUsed;
    }

    public void setLicenseUsed(boolean licenseUsed) {
        this.licenseUsed = licenseUsed;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public boolean isManuallyAdded() {
        return manuallyAdded;
    }

    public void setManuallyAdded(boolean manuallyAdded) {
        this.manuallyAdded = manuallyAdded;
    }

    public int getMaxArchiveDays() {
        return maxArchiveDays;
    }

    public void setMaxArchiveDays(int maxArchiveDays) {
        this.maxArchiveDays = maxArchiveDays;
    }

    public int getMinArchiveDays() {
        return minArchiveDays;
    }

    public void setMinArchiveDays(int minArchiveDays) {
        this.minArchiveDays = minArchiveDays;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMotionMask() {
        return motionMask;
    }

    public void setMotionMask(String motionMask) {
        this.motionMask = motionMask;
    }

    public String getMotionType() {
        return motionType;
    }

    public void setMotionType(String motionType) {
        this.motionType = motionType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPhysicalId() {
        return physicalId;
    }

    public void setPhysicalId(String physicalId) {
        this.physicalId = physicalId;
    }

    public String getPreferredServerId() {
        return preferredServerId;
    }

    public void setPreferredServerId(String preferredServerId) {
        this.preferredServerId = preferredServerId;
    }

    public boolean isScheduleEnabled() {
        return scheduleEnabled;
    }

    public void setScheduleEnabled(boolean scheduleEnabled) {
        this.scheduleEnabled = scheduleEnabled;
    }

    public String getSecondaryStreamQuality() {
        return secondaryStreamQuality;
    }

    public void setSecondaryStreamQuality(String secondaryStreamQuality) {
        this.secondaryStreamQuality = secondaryStreamQuality;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusFlags() {
        return statusFlags;
    }

    public void setStatusFlags(String statusFlags) {
        this.statusFlags = statusFlags;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserDefinedGroupName() {
        return userDefinedGroupName;
    }

    public void setUserDefinedGroupName(String userDefinedGroupName) {
        this.userDefinedGroupName = userDefinedGroupName;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public List<AddParamsBean> getAddParams() {
        return addParams;
    }

    public void setAddParams(List<AddParamsBean> addParams) {
        this.addParams = addParams;
    }

    public List<?> getScheduleTasks() {
        return scheduleTasks;
    }

    public void setScheduleTasks(List<?> scheduleTasks) {
        this.scheduleTasks = scheduleTasks;
    }

    public static class AddParamsBean {
        /**
         * name : DeviceUrl
         * value : http://192.168.1.231:8899/onvif/device_service
         */

        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
