# demo-starters

This is a demo project to demo the use of starters for your application.
It contains implementations to show the use of openapi, generic interceptors, oauth2 configuration.
The implementation of this starter is done in the [demo-use-starters] project.

NOTE: a library is also added here for simplicity, it will usually be a separate project but it's important to see 
the difference between a starter and a library and when to use which.

A brief explanation of advantages and disadvantages of using starters:
- Advantages:
  - Easy to use, maintain, update, add new features, remove features
  - Reduces code in your microservices and reduces code duplications
  - Allows for standards within your application, e.g. logging standards
  - Allows for easy configuration of your application
- Disadvantages:
  - Added dependency to your microservices
  - Question of ownership in applications with multiple teams
  - Simplicity can sometimes make things more complicated, e.g.:
    - Putting a client in your starter means you might get duplicate domain objects and you will have to use an object mapper
    - OpenApi is great for most endpoints however if your request or response objects are too complex it might not be the best solution
      - This might also make you think that you would need to refactor that code but sometimes it's difficult to do so especially working code in production
  - It might be unclear what boiler plate code is used in your microservice, e.g. it's not obvious an oauth2 token is being used

Things to note on library vs starter:
- A starter allows configuration to be done within the starter & annotations you create
- A starter should be specific to your project, it is the basis for your microservices
- A library should be shared across teams/applications and should contain generic functionality
- You should have a clear view on the ownership of libraries and always make sure it's innersource to reduce dependencies on another team