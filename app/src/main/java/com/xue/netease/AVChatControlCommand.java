package com.xue.netease;

/**
 * 不可用
 * <p>
 * byte UNKNOWN = -1;
 * byte NOTIFY_AUDIO_ON = 1;
 * byte NOTIFY_AUDIO_OFF = 2;
 * byte NOTIFY_VIDEO_ON = 3;
 * byte NOTIFY_VIDEO_OFF = 4;
 * byte SWITCH_AUDIO_TO_VIDEO = 5;
 * byte SWITCH_AUDIO_TO_VIDEO_AGREE = 6;
 * byte SWITCH_AUDIO_TO_VIDEO_REJECT = 7;
 * byte SWITCH_VIDEO_TO_AUDIO = 8;
 * byte BUSY = 9;
 * byte START_NOTIFY_RECEIVED = 12;
 * byte NOTIFY_RECORD_START = 13;
 * byte NOTIFY_RECORD_STOP = 14;
 * byte NOTIFY_CUSTOM_BASE = 64;
 */
public interface AVChatControlCommand {
    byte SWITCH_AUDIO_TO_VIDEO = 21;
    byte SWITCH_AUDIO_TO_VIDEO_AGREE = 22;
    byte SWITCH_AUDIO_TO_VIDEO_REJECT = 23;

    byte SWITCH_VIDEO_TO_AUDIO = 31;
    byte SWITCH_VIDEO_TO_AUDIO_AGREE = 32;
    byte SWITCH_VIDEO_TO_AUDIO_REJECT = 33;
}