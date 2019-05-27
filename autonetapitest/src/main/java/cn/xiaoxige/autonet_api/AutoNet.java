package cn.xiaoxige.autonet_api;

import java.util.Map;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetRequest;
import io.reactivex.FlowableTransformer;

/**
 * @author xiaoxige
 * @date 2019/5/26 4:41 PM
 * -
 * email: xiaoxigexiaoan@outlook.com
 * desc: TODO
 */
public class AutoNet {

    public static AutoNet getInstance() {
        return new AutoNet();
    }

    public void test(Object o, int type) {

    }

    public final void startNet(IAutoNetRequest requestEntity, Map requestMap,
                               String extraDynamicParam, String suffixUrl,
                               Integer flag, long writeOutTime, long readOutTime,
                               long connectOutTime, String domainNameKey,
                               String disposableBaseUrl, Map disposableHeads,
                               long encryptionKey, Boolean isEncryption, String mediaType,
                               AutoNetPatternAnontation.NetPattern netPattern, String responseClazzName,
                               AutoNetStrategyAnontation.NetStrategy netStrategy, String targetClazzName,
                               AutoNetTypeAnontation.Type reqType,
                               AutoNetTypeAnontation.Type resType, String pushFileKey,
                               String filePath, String fileName, IAutoNetCallBack callBack, FlowableTransformer transformer) {
    }

}
