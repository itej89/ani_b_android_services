package fouriermachines.ani.client.nexcloud.Framework;

import com.owncloud.android.lib.common.network.OnDatatransferProgressListener;

public class OnCloudDatatransferProgressListener implements OnDatatransferProgressListener {


    @Override
    public void onTransferProgress(long progressRate, long totalTransferredSoFar, long totalToTransfer, String fileName) {
        final long percentage = (totalToTransfer > 0 ? totalTransferredSoFar * 100 / totalToTransfer : 0);

    }
}
