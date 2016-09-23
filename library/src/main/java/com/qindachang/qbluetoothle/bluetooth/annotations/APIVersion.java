package com.qindachang.qbluetoothle.bluetooth.annotations;

import android.support.annotation.IntDef;

import com.qindachang.qbluetoothle.bluetooth.constant.Version;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * Created by qin da chang on 2016/9/23.
 */
@IntDef(value = {Version.AUTO, Version.JELLY_BEAN, Version.LOLLIPOP})
@Retention(RetentionPolicy.SOURCE)
public @interface APIVersion {

}
