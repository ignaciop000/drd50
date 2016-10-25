package com.datecs.audioreader.emv;

public interface EMVProcessorCallback {
    byte[] onCardHolderSelectionRequest(byte[] bArr);

    void onConfirmOrReverseOnlineRequest(byte[] bArr);

    byte[] onOnlineProcessingRequest(byte[] bArr);

    byte[] onPanCheckingRequest(byte[] bArr);
}
