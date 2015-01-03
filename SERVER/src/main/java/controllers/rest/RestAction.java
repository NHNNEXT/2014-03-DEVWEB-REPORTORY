package controllers.rest;

import autumn.Result;
import util.JsonResult;
import util.exceptions.BadRequestException;
import util.exceptions.ForbiddenException;
import util.exceptions.InternalServerErrorException;
import util.exceptions.NotFoundException;

import java.sql.SQLException;

public class RestAction {
    public interface RestActionWrapper {
        public Result restAction() throws BadRequestException, ForbiddenException, NotFoundException, InternalServerErrorException, SQLException;
    }

    public static Result doAction(RestActionWrapper restActionWrapper) {
        try {
            return restActionWrapper.restAction();
        } catch (BadRequestException e) {
            // 400 Bad Request
            return Result.BadRequest.json(new JsonResult(e.getLocalizedMessage()));
        } catch (ForbiddenException e) {
            // 403 Forbidden
            return Result.Forbidden.json(new JsonResult(e.getLocalizedMessage()));
        } catch (NotFoundException e) {
            // 404 Not Found
            return Result.NotFound.json(new JsonResult(e.getLocalizedMessage()));
        } catch (InternalServerErrorException | SQLException e) {
            // 500 Internal Server Error
            return Result.InternalServerError.json(new JsonResult(e.getLocalizedMessage()));
        }
    }
}
