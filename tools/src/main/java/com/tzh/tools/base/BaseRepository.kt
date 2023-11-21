package com.tzh.tools.base

import com.tzh.tools.util.getClazz


/**
 * Created by xwm on 2021/7/2
 */
open class BaseRepository<D : BaseDao> {

    protected val mDao: D by lazy {
        (getClazz<D>(this))
            .getDeclaredConstructor()
            .newInstance()
    }
}