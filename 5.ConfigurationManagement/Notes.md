124. Introduction to configuration

It is managing and controlling the configurations of each microservices in the system
It might be details as database connections, external service URLs, caching Settings etc...

Challenges
- Environment management - handling prod , dev, qa environment 
Security and Sensitive Data - hiding the password of db etc
consistency and centralization - require to avoid writing the common configuration of all microservices and we can manage by changing in one file. we can use git.
Dynamic Updates and High Availability - handling when there are bulk users
Monitoring and Versioning - handling configurations