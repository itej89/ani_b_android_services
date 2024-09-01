package fouriermachines.ani.client.nexcloud.FrameworkInterface;


import android.net.Uri;
import android.os.Handler;

import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.OwnCloudClientFactory;
import com.owncloud.android.lib.common.OwnCloudCredentialsFactory;
import com.owncloud.android.lib.common.operations.RemoteOperation;
import com.owncloud.android.lib.common.operations.RemoteOperationResult;
import com.owncloud.android.lib.resources.files.CreateFolderRemoteOperation;
import com.owncloud.android.lib.resources.files.DownloadFileRemoteOperation;
import com.owncloud.android.lib.resources.files.FileUtils;
import com.owncloud.android.lib.resources.files.ReadFolderRemoteOperation;
import com.owncloud.android.lib.resources.files.RemoveFileRemoteOperation;
import com.owncloud.android.lib.resources.files.UploadFileRemoteOperation;
import com.owncloud.android.lib.resources.files.model.RemoteFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import Framework.DataTypes.GlobalContext;
import fouriermachines.ani.client.nexcloud.Framework.Delegates.cloudOperationStatusNotify;
import fouriermachines.ani.client.nexcloud.Framework.Delegates.nexCloudUploadNotify;
import fouriermachines.ani.client.nexcloud.Framework.OnCloudDatatransferProgressListener;
import fouriermachines.ani.client.nexcloud.Framework.OnCloudRemoteOperationListener;
import fouriermachines.ani.client.nexcloud.R;

public class NexCloudManager implements cloudOperationStatusNotify {

    private OwnCloudClient mClient;
    private Handler mHandler;
    OnCloudDatatransferProgressListener _OnCloudDatatransferProgressListener;
    OnCloudRemoteOperationListener _OnCloudRemoteOperationListener;
    nexCloudUploadNotify _nexCloudUploadNotify;

    public NexCloudManager(nexCloudUploadNotify onNexCloudUploadNotify)
    {
        _nexCloudUploadNotify = onNexCloudUploadNotify;
        _OnCloudDatatransferProgressListener = new OnCloudDatatransferProgressListener();

        _OnCloudRemoteOperationListener = new OnCloudRemoteOperationListener(this);

        mHandler = new Handler();
        Uri serverUri = Uri.parse(GlobalContext.context.getString(R.string.server_base_url));
        mClient = OwnCloudClientFactory.createOwnCloudClient(serverUri, GlobalContext.context, true);



        mClient.setCredentials(
                OwnCloudCredentialsFactory.newBasicCredentials(
                        GlobalContext.context.getString(R.string.username),
                        GlobalContext.context.getString(R.string.password)
                )
        );
    }


    HashMap<String , ArrayList<String>> DirectoryList;
    Iterator entriesDirectoryList;
    Map.Entry<String , ArrayList<String>> entry_Directory;
    int FileIndex;

    enum UploadStates {IDLE, UPLOAD_DIRECTORY, WAIT_UPLOAD_DIRECTORY, DONE_UPLOAD_DIRECTORY,
        UPLOAD_FILE, WAIT_UPLOAD_FILE, DONE_UPLOAD_FILE, FINISH};

    UploadStates UploadState = UploadStates.IDLE;
    String Unique_ID = UUID.randomUUID().toString();

    public void BeginDirectoryUpload(HashMap<String , ArrayList<String>> _DirectoryList)
    {
        DirectoryList = _DirectoryList;
        UploadState = UploadStates.IDLE;
        DoUpload();
    }

    public void cloudOperationStatus(String Id, boolean Status)
    {
        if(Unique_ID == Id)
            DoUpload();
    }

    public void DoUpload()
    {
        switch (UploadState)
        {
            case IDLE:
                entriesDirectoryList = DirectoryList.entrySet().iterator();
                UploadState = UploadStates.UPLOAD_DIRECTORY;
                DoUpload();
                break;
            case UPLOAD_DIRECTORY:
               if(!entriesDirectoryList.hasNext()) {
                   UploadState = UploadStates.FINISH;
                   DoUpload();
               }
               else {
                   entry_Directory =  (Map.Entry) entriesDirectoryList.next();
                   FileIndex = 0;
                   UploadState = UploadStates.WAIT_UPLOAD_DIRECTORY;
                   startCreateDirectory(entry_Directory.getKey().toString());
               }
                break;
            case WAIT_UPLOAD_DIRECTORY:
                UploadState = UploadStates.DONE_UPLOAD_DIRECTORY;
                DoUpload();
                break;
            case DONE_UPLOAD_DIRECTORY:
                UploadState = UploadStates.UPLOAD_FILE;
                DoUpload();
                break;
            case UPLOAD_FILE:
                if(entry_Directory.getValue().size() == FileIndex) {
                    UploadState = UploadStates.UPLOAD_DIRECTORY;
                    DoUpload();
                }
                else {
                    UploadState = UploadStates.WAIT_UPLOAD_FILE;
                    startUploadFile(entry_Directory.getKey(), entry_Directory.getValue().get(FileIndex++), "application/zip");
                }
                    break;
            case WAIT_UPLOAD_FILE:
                UploadState = UploadStates.DONE_UPLOAD_FILE;
                DoUpload();
                break;
            case DONE_UPLOAD_FILE:
                UploadState = UploadStates.UPLOAD_FILE;
                DoUpload();
                break;
            case FINISH:
                break;
        }
    }


    public void startCreateDirectory(String remoteDirectoryPath) {
        CreateFolderRemoteOperation createFolderRemoteOperation =
                new CreateFolderRemoteOperation(remoteDirectoryPath, true);
        Unique_ID = UUID.randomUUID().toString();
        _OnCloudRemoteOperationListener.Operation_ID = Unique_ID;
        createFolderRemoteOperation.execute(mClient, _OnCloudRemoteOperationListener, mHandler);
    }

    public void startUploadFile(String remoteDirectory, String FilePath, String mimeType) {
        File fileToUpload = new File(FilePath);
        String remotePath = remoteDirectory +FileUtils.PATH_SEPARATOR + fileToUpload.getName();

        // Get the last modification date of the file from the file system
        Long timeStampLong = fileToUpload.lastModified() / 1000;
        String timeStamp = timeStampLong.toString();


        UploadFileRemoteOperation uploadOperation =
                new UploadFileRemoteOperation(FilePath, remotePath, mimeType, timeStamp);
        uploadOperation.addDataTransferProgressListener(_OnCloudDatatransferProgressListener);
        Unique_ID = UUID.randomUUID().toString();
        _OnCloudRemoteOperationListener.Operation_ID = Unique_ID;
        uploadOperation.execute(mClient, _OnCloudRemoteOperationListener, mHandler);
    }


}
