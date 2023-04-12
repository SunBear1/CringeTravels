import json

import requests
from fastapi import APIRouter, Response, Depends, status
from starlette.responses import JSONResponse

from common.authentication import oauth2_scheme, verify_jwt_token
from common.constants import RESERVATIONS_MS_ADDRESS
from users.service import verify_user_identify

router = APIRouter(prefix="/api/v1/reservations")


@router.post("/{trip_id}",
             responses={
                 201: {"description": "Reservation successfully created"},
                 403: {"description": "User does not have permission to use this service"},
                 404: {"description": "Trip with provided ID does not exist"},
                 422: {"description": "Unknown error occurred"}
             },
             )
async def make_reservation(trip_id: str, token: str = Depends(oauth2_scheme)):
    """
    Make a trip reservation
    """
    try:
        users_credentials = verify_jwt_token(token=token)
        if not verify_user_identify(login=users_credentials["login"], password=users_credentials["password"]):
            return Response(status_code=status.HTTP_403_FORBIDDEN,
                            content="User does not have permission to use this service", media_type="text/plain")

        response = requests.post(f"http://{RESERVATIONS_MS_ADDRESS}/api/v1/reservation/{trip_id}",
                                 timeout=3.00,
                                 verify=False)

        if response.status_code == status.HTTP_201_CREATED:
            return JSONResponse(status_code=status.HTTP_201_CREATED,
                                content=json.loads(response.content.decode("utf-8")),
                                media_type="application/json")
        if response.status_code == status.HTTP_404_NOT_FOUND:
            return Response(status_code=status.HTTP_404_NOT_FOUND, content=f"Trip with ID {trip_id} does not exist",
                            media_type="text/plain")
        if response.status_code == status.HTTP_500_INTERNAL_SERVER_ERRO:
            return Response(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, content="Reservation service crashed :-)",
                            media_type="text/plain")

    except requests.exceptions.ConnectionError:
        return Response(status_code=status.HTTP_503_SERVICE_UNAVAILABLE, content="Can't connect to reservation service",
                        media_type="text/plain")
    except Exception as ex:  # TODO to exception będzie zmienione na bardziej konkretne kiedy powstanie reservation service
        raise ex
        # return Response(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, content=f"Something went wrong",
        #                 media_type="text/plain")
