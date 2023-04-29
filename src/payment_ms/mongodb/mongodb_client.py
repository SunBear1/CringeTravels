import logging
import os

from pymongo import MongoClient

PORT = os.getenv("MONGODB_PORT", 27017)
HOST = os.getenv("MONGODB_ADDRESS", "localhost")
USER = os.getenv("MONGODB_USER", "admin")
PASSWD = os.getenv("MONGODB_PASSWORD", "admin")
DB_NAME = os.getenv("MONGODB_DB", "payments_db")

PAYMENTS_COLLECTION_NAME = "payments"

logger = logging.getLogger("payments")


class MongoDBClient:
    client = MongoClient(f"mongodb://{USER}:{PASSWD}@{HOST}:{PORT}/?authSource=admin", connectTimeoutMS=10000)
    db = client[DB_NAME]
    payments_collection = db[PAYMENTS_COLLECTION_NAME]
    logger.info(f"Connection to mongoDB at {HOST}:{PORT} established.")
