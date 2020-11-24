// IRemoteService.aidl
package com.chris.android_samples.service;

import com.chris.android_samples.service.IRemoteServiceListener;

interface IRemoteService {
    long getRemainingTime();

    int getPid();

    void setRemainingTimeListener(IRemoteServiceListener listener);
}
