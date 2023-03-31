import uvicorn
from fastapi import FastAPI, APIRouter

from endpoints.events import router as event_router
from endpoints.payments import router as payments_router
from endpoints.purchases import router as purchases_router
from endpoints.reservations import router as reservations_router
from endpoints.trips import router as trips_router
from endpoints.users import router as users_router

app = FastAPI()

api_router = APIRouter()

app.include_router(users_router, tags=["Endpoints for user operations"])
app.include_router(payments_router, tags=["Endpoints for payment ms operations"])
app.include_router(purchases_router, tags=["Endpoints for purchase ms operations"])
app.include_router(reservations_router, tags=["Endpoints for reservations ms operations"])
app.include_router(trips_router, tags=["Endpoints for trip researcher operations"])
app.include_router(event_router, tags=["Endpoints for status events operations"])
app.include_router(api_router)

if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=8000)
