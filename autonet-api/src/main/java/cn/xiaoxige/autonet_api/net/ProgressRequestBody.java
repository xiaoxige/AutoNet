package cn.xiaoxige.autonet_api.net;

import java.io.File;
import java.io.IOException;

import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 *         RequestBody with the pace of the callback file.
 */

public class ProgressRequestBody {

    public static RequestBody createProgressRequestBody(final MediaType contentType, final File file, final IAutoNetFileCallBack callBack) {

        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                source = Okio.source(file);
                Buffer buf = new Buffer();
                long remaining = contentLength();
                long current = 0;
                float preProgress = 0;
                float progress = 0;
                for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                    sink.write(buf, readCount);
                    current += readCount;
                    progress = current * 100 / remaining;
                    if (preProgress != progress) {
                        if (callBack != null) {
                            callBack.onPregress(progress);
                        }
                    }
                    preProgress = progress;
                }

                if (callBack != null) {
                    callBack.onComplete(file);
                }
            }
        };
    }
}
