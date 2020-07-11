# To get all parking slots
curl --location --request GET 'localhost:8080/slots'

# To add parking slot
curl --location --request POST 'localhost:8080/add-slot' --header 'Content-Type: application/json' --data-raw '{
	"id":123456,
	"state" : "Occupied"
}'

# To update parking slot
curl --location --request POST 'localhost:8080/update-slot' --header 'Content-Type: application/json' --data-raw '{
	"id":123456,
	"state" : "Free"
}'
