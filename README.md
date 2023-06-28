# Hunter Six - Java Spring RESTful API Test

## How to build
```./gradlew clean build```

## How to test
```./gradlew test```

## Exercises
### Exercise 1
Make the tests run green (there should be one failing test)

-Added static keyword with counter object in Person class, so that it will get created once at the time of class loading and get shared with all object.

### Exercise 2
Update the existing `/person/{lastName}/{firstName}` endpoint to return an appropriate RESTful response when the requested person does not exist in the list
- prove your results

It can be implemented in 2 ways - 
1. Return ResponseEntity - For errors, add proper status code and return with empty body.
   1) if requested person does not exist in the list, return Response Entity with response status code as **Not_Found 404**


2. Return Objects - Create exceptions with proper error messages, add them to Common controller Advice class  and throw.
   1) If we get person, as we have @RestController annotation return Person object. 
   2) If person does not exist in the list, create PersonNotFoundException class by extending RuntimeException with error message and status code **Not_Found 404**. 
   Throw PersonNotFoundException. 
   
#### _By 2nd approach, we can get proper error message with response status 404. But to keep common approach/coding style(as per exercise 3) for all API's, I used 1st approach._


### Exercise 3
Write a RESTful API endpoint to retrieve a list of all people with a particular surname
- pay attention to what should be returned when there are no match, one match, multiple matches
- prove your results

It can be implemented in 2 ways -
1. Return ResponseEntity - Add proper status code and return with empty body.
   1) If we get one or multiple matches, return ResponseEntity with Person list as a body (ResponseEntity<List<Person>>) and status code 200.
   2) If there is no match, return ResponseEntity with empty body and response status code as **No_Content 204**.


2. Return Objects - Create exceptions with proper error messages, add them to Common controller Advice class and throw.
   1) If we get matched persons, as we have @RestController annotation return List of Person object
   2) If not, create EmptyResponseException class by extending RuntimeException and add to Controller Advice class with response status code No_Content **204**. 
   Throw EmptyResponseException if we get empty list.
   
#### _I did not use 2nnd approach because I think, empty response is not the error as request get executes successfully. So implemented using 1st approach._


### Exercise 4
Write a RESTful API endpoint to add a new value to the list
- pay attention to what should be returned when the record already exists
- prove your results

1. Return ResponseEntity - Add proper status code and return with empty body.
   1) If user provides new person's details, save the person and return Response Entity with new person's uri and status code **Created 201**.
   2) If user provides existing person's details then return ResponseEntity with empty body and response status code as **Conflict 409**.
   3) If user provides empty first name or last name, return ResponseEntity with empty body and response status code as **Bad_Request 400**.


3. Return Objects - Create exceptions with proper error messages, add them to Common controller Advice class and throw.
   1) If we get valid new person's details, as we have @RestController annotation return saved Person object.
   2) If user provides  empty first name or last name, create BadRequestException class by extending RuntimeException with proper error messages add it to Controller Advice class.
   Throw it with response status code as **Bad_Request 400** and error messages.
   3) If user provides existing person's details, create ConflictException class by extending RuntimeException add it to Controller Advice class. 
   Throw it with response status code as **Conflict 409**.

#### _I did not use 2nd approach to be inline with get list of people api(exercise 3)_