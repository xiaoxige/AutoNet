package cn.xiaoxige.autonet_api.repository.impl;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.client.Client;
import cn.xiaoxige.autonet_api.constant.AutoNetConstant;
import cn.xiaoxige.autonet_api.error.CutOffError;
import cn.xiaoxige.autonet_api.error.EmptyError;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetBodyCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetHeadCallBack;
import cn.xiaoxige.autonet_api.net.ProgressRequestBody;
import cn.xiaoxige.autonet_api.repository.AutoNetRepo;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author xiaoxige
 * @date 2019/6/17 0017 15:36
 * -
 * email: xiaoxigexiaoan@outlook.com
 * desc: Implementation of AutoNet Single Complete Request
 */
public class AutoNetRepoImpl<T> implements AutoNetRepo<T> {

    private String mUrl;
    private Map<String, Object> mHeads;
    private Map mParams;
    private Object mFlag;
    private String mMediaType;
    private Class<T> mResponseClass;
    private AutoNetTypeAnontation.Type mReqType;
    private IAutoNetBodyCallBack mBodyCallBack;

    private OkHttpClient client;

    public AutoNetRepoImpl(String url, Map<String, Object> heads, Map params, Object flag, long writeOutTime, long readOutTime, long connectOutTime,
                           long encryptionKey, Boolean isEncryption,
                           String mediaType,
                           Class<T> responseClazz,
                           AutoNetTypeAnontation.Type reqType,
                           List<Interceptor> interceptors,
                           IAutoNetEncryptionCallback encryptionCallback, IAutoNetHeadCallBack headCallBack, IAutoNetBodyCallBack bodyCallBack) {
        this.mUrl = url;
        this.mHeads = heads;
        this.mParams = params;
        this.mFlag = flag;
        this.mMediaType = mediaType;
        this.mResponseClass = responseClazz;
        this.mReqType = reqType;
        this.mBodyCallBack = bodyCallBack;

        this.client = Client.client(flag, heads, writeOutTime, readOutTime, connectOutTime, encryptionKey, isEncryption, interceptors, encryptionCallback, headCallBack);
    }

    @Override
    public T doNetGet() throws Exception {
        String newUrl = restructureUrlWithParams(this.mUrl, this.mParams);
        Request request = new Request.Builder()
                .get()
                .url(newUrl)
                .build();

        return executeNet(request);
    }

    @Override
    public T doNetPost() throws Exception {
        String bodyParams = structureBodyParams(this.mReqType, this.mParams);
        RequestBody body = RequestBody.create(MediaType.parse(this.mMediaType), bodyParams);
        Request request = new Request.Builder().url(this.mUrl).post(body).build();

        return executeNet(request);
    }

    @Override
    public T doPut() throws Exception {

        String bodyParams = structureBodyParams(this.mReqType, this.mParams);
        RequestBody body = RequestBody.create(MediaType.parse(this.mMediaType), bodyParams);
        Request request = new Request.Builder().url(this.mUrl).put(body).build();

        return executeNet(request);
    }

    @Override
    public T doDelete() throws Exception {
        String newUrl = restructureUrlWithParams(this.mUrl, this.mParams);
        Request request = new Request.Builder()
                .delete()
                .url(newUrl)
                .build();

        return executeNet(request);
    }

    @Override
    public T pushFile(String pushFileKey, String filePath, final IAutoNetFileCallBack callBack) throws Exception {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (this.mParams != null) {
            //noinspection unchecked
            Set<String> keys = this.mParams.keySet();
            for (String key : keys) {
                Object value = this.mParams.get(key);
                if (value == null) {
                    continue;
                }
                builder.addFormDataPart(key, value.toString());
            }
        }

        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("File uploading files do not exist.");
        }

        builder.addFormDataPart(pushFileKey, file.getName(),
                ProgressRequestBody.createProgressRequestBody(MediaType.parse(this.mMediaType), file, new IAutoNetFileCallBack() {
                    @Override
                    public void onProgress(float progress) {
                        //noinspection unchecked
                        if (callBack != null) {
                            callBack.onProgress(progress);
                        }
                    }

                    @Override
                    public void onComplete(File file) {
                        //noinspection unchecked
                        if (callBack != null) {
                            callBack.onComplete(file);
                        }
                    }
                }));

        Request request = new Request.Builder().url(this.mUrl).post(builder.build())
                .build();

        return executeNet(request);
    }

    @Override
    public T pullFile(String filePath, String fileName, IAutoNetFileCallBack callBack) throws Exception {
        String bodyParams = structureBodyParams(this.mReqType, this.mParams);
        RequestBody body = RequestBody.create(MediaType.parse(mMediaType), bodyParams);
        Request request = new Request.Builder().url(this.mUrl).post(body).build();
        Response response = client.newCall(request).execute();
        if (response == null) {
            throw new EmptyError();
        }

        File file = new File(filePath);
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        file = new File(filePath + File.separator + fileName);
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        }

        //noinspection unchecked
        return (T) recvFile(response, file, callBack);
    }

    private T executeNet(Request request) throws Exception {
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        if (body == null) {
            throw new EmptyError();
        }

        String content = body.string();
        if (this.mBodyCallBack != null) {
            if (this.mBodyCallBack.body(this.mFlag, response.code(), content)) {
                throw new CutOffError();
            }
        }

        if (TextUtils.isEmpty(content)) {
            throw new EmptyError();
        }

        if (String.class.equals(this.mResponseClass) || Object.class.equals(this.mResponseClass)) {
            // Return String type body data directly
            //noinspection unchecked
            return (T) content;
        } else {
            return new Gson().fromJson(content, this.mResponseClass);
        }

    }

    /**
     * Parameter processing (mainly for Get, Delete requests)
     *
     * @param url
     * @param params
     * @return
     */
    private String restructureUrlWithParams(String url, Map params) {
        if (params == null || params.isEmpty()) {
            return url;
        }

        //noinspection unchecked
        Set<String> keys =
                params.keySet();
        //noinspection StringBufferMayBeStringBuilder
        StringBuffer paramsBuffer = new StringBuffer(url);
        int i = 0;
        for (String key : keys) {
            if (i == 0 && !url.contains(AutoNetConstant.PARAMETER_START_FLAG)) {
                paramsBuffer.append(AutoNetConstant.PARAMETER_START_FLAG);
            } else {
                paramsBuffer.append(AutoNetConstant.PARAMETRIC_LINK_MARKER);
            }
            Object value = params.get(key);
            //noinspection StringConcatenationInsideStringBufferAppend
            paramsBuffer.append(key + "=" + value);
            i++;
        }
        url = paramsBuffer.toString();
        if (url.endsWith(AutoNetConstant.PARAMETRIC_LINK_MARKER)) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     * Parameter processing (mainly for Post, put requests)
     *
     * @param reqType
     * @param requestParams
     * @return
     */
    private String structureBodyParams(AutoNetTypeAnontation.Type reqType, Map requestParams) {
        if (reqType == AutoNetTypeAnontation.Type.JSON) {
            return new Gson().toJson(requestParams);
        } else if (reqType == AutoNetTypeAnontation.Type.FORM) {
            return structureFormParams(requestParams);
        } else {
            // Using JSON format by default
            return new Gson().toJson(requestParams);
        }
    }

    /**
     * Structure form parameter
     *
     * @param requestParams
     * @return
     */
    private String structureFormParams(Map requestParams) {
        if (requestParams == null || requestParams.size() <= 0) {
            return "";
        }
        //noinspection StringBufferMayBeStringBuilder
        StringBuffer buffer = new StringBuffer();
        //noinspection unchecked
        Set<String> keys = requestParams.keySet();
        for (String key : keys) {
            //noinspection StringConcatenationInsideStringBufferAppend
            buffer.append(key + "=" + requestParams.get(key) + AutoNetConstant.PARAMETRIC_LINK_MARKER);
        }
        String bodyParams = buffer.toString();
        if (bodyParams.endsWith(AutoNetConstant.PARAMETRIC_LINK_MARKER)) {
            bodyParams = bodyParams.substring(0, bodyParams.length() - 1);
        }
        return bodyParams;
    }


    private File recvFile(Response response, File file, IAutoNetFileCallBack callBack) throws IOException {
        //noinspection ConstantConditions
        long fileSize = response.body().contentLength();
        //noinspection ConstantConditions
        InputStream is =
                response.body().byteStream();

        float preProgress = 0;
        float progress;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            long pullLength = 0;
            byte[] b = new byte[AutoNetConstant.DEFAULT_BYBE_SIZE];
            int len;
            while ((len = is.read(b)) != -1) {
                fos.write(b, 0, len);
                pullLength += len;
                progress = (int) (pullLength * AutoNetConstant.MAX_PROGRESS / fileSize);
                if (preProgress != progress && Math.abs(progress - preProgress) >= 1) {
                    //noinspection unchecked
                    if (callBack != null) {
                        callBack.onProgress(progress);
                    }
                    preProgress = progress;
                }
            }

            if (callBack != null) {
                callBack.onComplete(file);
            }

            fos.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return file;
    }

}
