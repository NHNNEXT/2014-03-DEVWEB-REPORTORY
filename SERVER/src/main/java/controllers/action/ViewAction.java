package controllers.action;

import autumn.Request;
import autumn.Result;
import autumn.result.TemplateResult;
import controllers.services.UserService;
import util.exceptions.BadRequestException;
import util.exceptions.ForbiddenException;
import util.exceptions.InternalServerErrorException;
import util.exceptions.NotFoundException;

import java.sql.SQLException;

public class ViewAction {
    public interface ViewActionWrapper {
        public TemplateResult viewAction() throws BadRequestException, ForbiddenException, NotFoundException, InternalServerErrorException, SQLException;
    }

    private static final String ERROR_TEMPLATE_NAME = "error";
    private static final String ERROR_MESSAGE_VARIABLE_NAME = "errorMessage";

    private static final String LOGIN_USER_VARIABLE_NAME = "loginUser";

    public static TemplateResult doAction(ViewActionWrapper viewActionWrapper) {
        try {
            return viewActionWrapper.viewAction();
        } catch (BadRequestException e) {
            // 400 Bad Request
            return getErrorTemplateResult(Result.BadRequest, e.getLocalizedMessage());
        } catch (ForbiddenException e) {
            // 403 Forbidden
            return getErrorTemplateResult(Result.Forbidden, e.getLocalizedMessage());
        } catch (NotFoundException e) {
            // 404 Not Found
            return getErrorTemplateResult(Result.NotFound, e.getLocalizedMessage());
        } catch (InternalServerErrorException | SQLException e) {
            // 500 Internal Server Error
            return getErrorTemplateResult(Result.InternalServerError, e.getLocalizedMessage());
        }
    }

    public static Result doActionWithLoginUser(Request req, ViewActionWrapper viewActionWrapper) {
        return doAction(viewActionWrapper).withVariable(LOGIN_USER_VARIABLE_NAME, UserService.getUserLoginData(req));
    }

    private static TemplateResult getErrorTemplateResult(Result.StatusCodeHolder statusCodeHolder, String errorMessage) {
        return statusCodeHolder.template(ERROR_TEMPLATE_NAME).withVariable(ERROR_MESSAGE_VARIABLE_NAME, errorMessage);
    }
}
