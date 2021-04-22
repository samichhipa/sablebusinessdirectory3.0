package com.macinternetservices.sablebusinessdirectory.viewmodel.comment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.macinternetservices.sablebusinessdirectory.Config;
import com.macinternetservices.sablebusinessdirectory.repository.comment.CommentRepository;
import com.macinternetservices.sablebusinessdirectory.utils.AbsentLiveData;
import com.macinternetservices.sablebusinessdirectory.utils.Utils;
import com.macinternetservices.sablebusinessdirectory.viewmodel.common.PSViewModel;
import com.macinternetservices.sablebusinessdirectory.viewobject.Comment;
import com.macinternetservices.sablebusinessdirectory.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

public class CommentListViewModel extends PSViewModel {

    //for recent comment list

    public final String PRODUCT_ID_KEY = "itemId";
    public String itemId = "";

    private final LiveData<Resource<List<Comment>>> commentListData;
    private MutableLiveData<CommentListViewModel.TmpDataHolder> commentListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageCommentLoadingData;
    private MutableLiveData<CommentListViewModel.TmpDataHolder> nextPageLoadingStateObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> sendCommentHeaderPostData;
    private MutableLiveData<com.macinternetservices.sablebusinessdirectory.viewmodel.comment.CommentListViewModel.TmpDataHolder> sendCommentHeaderPostDataObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> commentCountLoadingData;
    private MutableLiveData<CommentListViewModel.TmpDataHolder> commentCountLoadingStateObj = new MutableLiveData<>();
    //region Constructor

    @Inject
    CommentListViewModel(CommentRepository commentRepository) {
        // Latest comment List
        commentListData = Transformations.switchMap(commentListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Comment List.");
            return commentRepository.getCommentList(Config.API_KEY, obj.itemId, obj.limit, obj.offset);
        });

        nextPageCommentLoadingData = Transformations.switchMap(nextPageLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Comment List.");
            return commentRepository.getNextPageCommentList(obj.itemId, obj.limit, obj.offset);
        });

        sendCommentHeaderPostData = Transformations.switchMap(sendCommentHeaderPostDataObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }
            return commentRepository.uploadCommentHeaderToServer(
                    obj.itemId,
                    obj.userId,
                    obj.headerComment,
                    obj.cityId);
        });

        commentCountLoadingData = Transformations.switchMap(commentCountLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Comment List.");
            return commentRepository.getCommentDetailReplyCount(obj.comment_id);
        });
    }

    //endregion
    public void setSendCommentHeaderPostDataObj(String itemId,
                                                String userId,
                                                String headerComment,
                                                String cityId
    ) {
        if (!isLoading) {
            com.macinternetservices.sablebusinessdirectory.viewmodel.comment.CommentListViewModel.TmpDataHolder tmpDataHolder = new com.macinternetservices.sablebusinessdirectory.viewmodel.comment.CommentListViewModel.TmpDataHolder();
            tmpDataHolder.itemId = itemId;
            tmpDataHolder.userId = userId;
            tmpDataHolder.headerComment = headerComment;
            tmpDataHolder.cityId = cityId;
            sendCommentHeaderPostDataObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getsendCommentHeaderPostData() {
        return sendCommentHeaderPostData;
    }


    //region Getter And Setter for Comment List

    public void setCommentListObj(String limit, String offset, String itemId) {
        if (!isLoading) {
            CommentListViewModel.TmpDataHolder tmpDataHolder = new CommentListViewModel.TmpDataHolder();
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            tmpDataHolder.itemId = itemId;
            commentListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<Comment>>> getCommentListData() {
        return commentListData;
    }

    //Get Comment Next Page
    public void setNextPageCommentLoadingObj(String itemId, String limit, String offset) {

        if (!isLoading) {
            CommentListViewModel.TmpDataHolder tmpDataHolder = new CommentListViewModel.TmpDataHolder();
            tmpDataHolder.limit = limit;
            tmpDataHolder.itemId = itemId;
            tmpDataHolder.offset = offset;
            nextPageLoadingStateObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public void setCommentCountLoadingObj(String comment_id) {

        if (!isLoading) {
            CommentListViewModel.TmpDataHolder tmpDataHolder = new CommentListViewModel.TmpDataHolder();
            tmpDataHolder.comment_id = comment_id;
            commentCountLoadingStateObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getCommentCountLoadingStateData() {
        return commentCountLoadingData;
    }


    public LiveData<Resource<Boolean>> getNextPageLoadingStateData() {
        return nextPageCommentLoadingData;
    }

    //endregion

    //region Holder
    class TmpDataHolder {
        public String limit = "";
        public String offset = "";
        public String itemId = "";
        public String userId = "";
        public String headerComment = "";
        public String comment_id = "";
        public Boolean isConnected = false;
        public String cityId;
    }
    //endregion
}
