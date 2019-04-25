package moshimoshi.cyplay.com.doublenavigation.model.events;

import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.BaseException;
import ninja.cyplay.com.apilibrary.domain.repositoryModel.exception.ExceptionType;
import ninja.cyplay.com.apilibrary.utils.StringUtils;

/**
 * Created by romainlebouc on 12/08/16.
 */
public class ResourceException {

    private ExceptionType exceptionType;
    private String status;
    private String message;

    public ResourceException(ExceptionType exceptionType, String status, String message) {
        this.exceptionType = exceptionType;
        this.status = status;
        this.message = message;
    }

    public ResourceException(BaseException e) {
        if (e != null) {
            this.exceptionType = e.getType();
            if (e.getResponse() != null){
                this.status = e.getResponse().getStatus();
                this.message = e.getResponse().getDetail();
            }
        }
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        String[] s = {status, message};
        return StringUtils.join(s, " ");
    }
}
