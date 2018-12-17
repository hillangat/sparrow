package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.entities.misc.AuditInfoBean;
import com.techmaster.sparrow.entities.misc.ErrorResponse;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.exception.SparrowRestfulApiException;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
import com.techmaster.sparrow.util.SparrowUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/")
public abstract class BaseController {

    public static final String SUCCESS_RETRIEVAL_MSG = "Successfully retrieved the data";
    public static final String SUCCESS_SAVED_MSG = "Saved successfully";
    public static final String SUCCESS_ACTION_COMPLETION = "Action completed successfully";
    public static final String FAILED_VALIDATION_MSG = "Action failed validation";

    @ExceptionHandler(SparrowRestfulApiException.class)
    public final ResponseEntity<ErrorResponse> handleException(SparrowRestfulApiException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    protected String getUserName() {
        Authentication authentication = getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        return null;
    }

    protected List<String> getUserRoles() {
        Authentication userDetails = getAuthentication();
        if (userDetails.getAuthorities() != null &&
                !userDetails.getAuthorities().isEmpty()) {

            return userDetails
                    .getAuthorities()
                    .stream()
                    .map(a -> a.getAuthority())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    protected <T extends AuditInfoBean> T addAuditInfo(T auditInfoBean) {
        SparrowUtil.addAuditInfo(auditInfoBean, getUserName());
        return auditInfoBean;
    }

    protected ResponseEntity<ResponseData> getResponse (boolean isGet, Object data, RuleResultBean ruleBean) {

        String retrievalCompletionMsg = isGet ? SUCCESS_RETRIEVAL_MSG : SUCCESS_ACTION_COMPLETION;

        if (ruleBean == null) {
            return ResponseEntity.ok(new ResponseData(data, retrievalCompletionMsg, Status.SUCCESS.getStatus(), null, 0));
        }

        String status = ruleBean.isSuccess() ? Status.SUCCESS.getStatus() : Status.FAILED.getStatus();

        String otherError = ruleBean.getErrors().containsKey(SparrowConstants.APPLICATION_ERROR_KEY)
                ? SparrowConstants.APPLICATION_ERROR_OCCURRED : FAILED_VALIDATION_MSG;

        String msg = ruleBean.isSuccess() ? ( retrievalCompletionMsg ) : otherError;

        return ResponseEntity.ok(new ResponseData(data, msg, status, ruleBean.getErrors(), 0));
    }

    protected boolean isAdmin() {
        return getUserRoles().stream().anyMatch(a -> a.equalsIgnoreCase("ADMIN"));
    }

}
