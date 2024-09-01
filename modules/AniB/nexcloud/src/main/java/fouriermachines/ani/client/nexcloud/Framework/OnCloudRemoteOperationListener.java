package fouriermachines.ani.client.nexcloud.Framework;

import com.owncloud.android.lib.common.operations.OnRemoteOperationListener;
import com.owncloud.android.lib.common.operations.RemoteOperation;
import com.owncloud.android.lib.common.operations.RemoteOperationResult;
import com.owncloud.android.lib.resources.files.CreateFolderRemoteOperation;
import com.owncloud.android.lib.resources.files.RemoveFileRemoteOperation;
import com.owncloud.android.lib.resources.files.UploadFileRemoteOperation;

import java.util.UUID;

import fouriermachines.ani.client.nexcloud.Framework.Delegates.cloudOperationStatusNotify;

public class OnCloudRemoteOperationListener implements OnRemoteOperationListener {

    cloudOperationStatusNotify notify_Status;
    public String Operation_ID = UUID.randomUUID().toString();
    public OnCloudRemoteOperationListener(cloudOperationStatusNotify _notify_Status)
    {
        notify_Status = _notify_Status;
    }

    @Override
    public void onRemoteOperationFinish(RemoteOperation operation, RemoteOperationResult result) {
        if (!result.isSuccess()){
            if(notify_Status != null)
                notify_Status.cloudOperationStatus(Operation_ID, false);
        }else if (operation instanceof CreateFolderRemoteOperation) {

            if(notify_Status != null)
                notify_Status.cloudOperationStatus(Operation_ID, true);

        }else if (operation instanceof UploadFileRemoteOperation) {

            if(notify_Status != null)
                notify_Status.cloudOperationStatus(Operation_ID, true);

        } else if (operation instanceof RemoveFileRemoteOperation) {

            if(notify_Status != null)
                notify_Status.cloudOperationStatus(Operation_ID, true);
        }
    }

    private void onSuccessfulUpload(UploadFileRemoteOperation operation, RemoteOperationResult result) {

    }

    private void onSuccessfulRemoteDeletion(RemoveFileRemoteOperation operation, RemoteOperationResult result) {

    }

}
