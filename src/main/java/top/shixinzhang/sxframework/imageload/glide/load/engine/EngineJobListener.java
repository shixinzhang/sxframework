package top.shixinzhang.sxframework.imageload.glide.load.engine;

import top.shixinzhang.sxframework.imageload.glide.load.Key;

interface EngineJobListener {

    void onEngineJobComplete(Key key, EngineResource<?> resource);

    void onEngineJobCancelled(EngineJob engineJob, Key key);
}
