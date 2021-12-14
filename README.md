# Solution to Project given by Rokomari.com for the post Backend Developer(Java)
## The problem description: 

Develop a backend with spring-boot which will provide rest-API support for a note-pad project.

The main features are:

- user can login/signup. After login, the valid user will receive an auth-token, which will be required to make other rest requests.

- user's token will be auto invalid after 5 mins or the user requested signout.

- User can have 1 of 2 roles. These roles are customer, admin.

- customer can only CRUD on notes of his/her notes. If he tries to delete any note, the status will be just updated.

- Admin can CRUD on all users notes.

- For reading operations, notes should be cached in Redis with time-to-live of 1 day for each note. notes should be retrieved from the cache if present. The cache should be updated on each database CRUD call.

- All valid data modification operations should be handled asynchronously via any message queue (ex: rabbitmq/ActiveMQ).

- There will be a special endPoint `/quote`, For which the backend will fetch a random quote from `https://api.quotable.io/random` & pass it as a response.
