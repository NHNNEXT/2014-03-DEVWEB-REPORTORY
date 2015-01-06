package controllers.action;

import autumn.Result;
import autumn.result.JsonResult;
import util.ResultResponse;
import util.exceptions.BadRequestException;
import util.exceptions.ForbiddenException;
import util.exceptions.InternalServerErrorException;
import util.exceptions.NotFoundException;

import java.sql.SQLException;

public class RestAction {
    public static JsonResult doAction(RestActionWrapper restActionWrapper) {
        try {
            return restActionWrapper.restAction();
        } catch (BadRequestException e) {
            // 400 Bad Request
            return Result.BadRequest.json(new ResultResponse(e.getLocalizedMessage()));
        } catch (ForbiddenException e) {
            // 403 Forbidden
            return Result.Forbidden.json(new ResultResponse(e.getLocalizedMessage()));
        } catch (NotFoundException e) {
            // 404 Not Found
            return Result.NotFound.json(new ResultResponse(e.getLocalizedMessage()));
        } catch (InternalServerErrorException | SQLException e) {
            // 500 Internal Server Error
            return Result.InternalServerError.json(new ResultResponse(e.getLocalizedMessage()));
        }
    }

    public interface RestActionWrapper {
        public JsonResult restAction() throws BadRequestException, ForbiddenException, NotFoundException, InternalServerErrorException, SQLException;
    }
}
