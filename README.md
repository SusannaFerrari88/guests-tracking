#Guests Tracking Tool
##HRS Innovation Club Software Challenge
###Overview

I’ve implemented a prototype in Java, following the Microservice architecture approach, but, in order to keep it simple to run and short, I’ve implemented everything in one single maven project, and one runnable service. 
In a real Microservice architecture, I would have separated the services, located them in two different repositories and managed the interaction with Rest APIs. 
In this example I just separated them locating them in different packages. 
It's interesting to notice, though, that the classes in the guest package don’t import any dependency from the classes in the parcel package, as it should be if they were divided in two microservices. 
I’ve implemented a couple of JUnit tests, just to show the possibilities of it, and to point out how easy and fast it is to write tests, when the services are small and simple, and the application levels are well divided. 
Ideally, each microservice should have its own DB. For this prototype I choose to skip the DB setup and opted for keeping track of the information with java objects.

###To run the project

Go into the project folder.
```
mvn clean install
cd target/
java -jar guests-tracking-1.0-SNAPSHOT.jar 
```

When the service is running, you can find the swagger page at : 
http://localhost:8080/swagger-ui.html

###Services interaction

This is a quite easy application, we have just one Api-gateway in place, which should be the only one that is visible from outside (but for this prototype I didn’t want to replicate any APIs so I kept public the ones inside of the two single microservices).
The Guest service doesn’t know about the existence of the Parcel service, and vice-versa. 
They don’t share any model class or service. 
The only one that imports classes from both of them is the api-gateway. 

###API flow

The swagger page is already a documentation of the APIs itself, but here I want to show a possible flow, in order to facilitate the testing part. 

At the beginning there are no guests, so the only thing you can do is create one:
```
guest-resource 
POST /guests
{
  	"firstName": "Susanna",
  	"lastName": "Ferrari",
  	"dateOfBirth": "1988-09-28",
	"checkIn": true
}
```

Here you can specify if you want to automatically check-in your guest or not (a hotel would typically want to store the guest information at the moment of the booking, and then check-in the user upon arrival).

If you set the check in flag to true, you can skip the check-in step :

```
guest-resource
POST /guests/{id}/check-in
```

Now you can try out the api-gateway API to get the guest info:

```
api-gateway-resource 
GET /public/guests ? lastName=“Ferrari” & firstName=“Susanna”
```

This endpoint returns the guest information, including the guest ID, needed for the next steps. (let’s say that it is 1)

When the parcel ABC arrives for the guest “Susanna Ferrari”, it’s possible then to accept it and store it (because the guest already checked-in and didn’t check-out yet), with the API: 

```
api-gateway-resource
POST /public/parcels 
{
  "code": "ABC",
  "guestId": 1
}
```

Now when you run again the get-guest-info API, you’ll see that it returns the just accepted parcel in the list of parcels to pick-up. 

At this point the guest also can’t check-out, because she has a “pending” parcel for her. 

In order to check-out the guest, she first has to pick-up all her parcels, calling the endpoint: 
```
parcel-resource 
POST /parcels/pick-up ? guestId = 1
```

Now you can finally call:
```
api-gateway-resource 
POST /public/guests/1/check-out
```

The endpoint that I didn’t mention are not strictly needed, I used them to check intermediate results, but I kept them in for completeness:
`GET /guests` -> to get a list of all guests / or filter them by name
`GET /parcels` -> to get a list of parcels / or filter them by guest
`GET /parcels/to-pick-up` -> to get a list of parcels to pick-up by a specific guest 

Important to notice is that all the endpoints that need coordination between the two services are passing through the api-gateway, while the ones that are getting or modifying the single entities can be called on the specific service. 

As I said, this is just a simplification done for this prototype, ideally the frontend will not even be able to reach the single microservices, and the api-gateway will have to expose all the endpoints that need to be used from outside. 
How I imagined the UI
This is how I imagined the UI to look like. I never start implementing the whole back-end without thinking about how the interface may be and how the user should interact with it. 
I’m no designer (as you probably noticed yourself), and this is just an example of the functionality of the app.
In the first screen you can search by name for the user.
You then end up in the second screen, where all the actions that act on a single guest can be performed (in this page, the guestId is known, because it was included in the search response, so it can be used for all the other API calls). 
In this specific case, the user is checked-in, so it’s possible to accept new parcels, but she still has two parcels to deliver, so it’s not possible to perform a check-out.

###Conclusion

This prototype was implemented in order to give an example solution to the given requirements.
Of course, all the decisions about the project structure and the endpoints specification, should be discussed with the team and also be based on possible future requirements. 
This project could have been much more complex and long, but I think it would have exceeded the purpose of demonstration.
It was fun to write it, though! :)
