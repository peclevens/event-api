# Requirements

\# | User story title | User story description | Priority | Notes
------------ | ------------- | ------------- | ------------- | -------------
1 | Find event | 1. A user wants to find an event by id.<br/>2. A user wants to list all created events. | Must have | The search feature should allow users to:<br/> 1. List events, sort them by start date-time and name ascendently and sort event sessions by level and duration ascendently by default.<br/> 2. Specify search terms.<br/> 3. Paginate and limit the result been returned.
2 | Create event | A user wants to create new event. | Must have | 1. User might specify one or more sessions when creating an event.
3 | Update event | A user wants to update existing event. | Must have | 1. User might update one or more sessions while updating event information.
4 | Delete event | 1. A user wants to delete an event by id.<br/> 2. A user wants to delete all events at once.| Must have | When deleting an event, all its sessions should be deleted as well.
5 | Find sessions for an event | 1. A user want to find a session by id. <br/>2. A user wants to list all sessions for a particular event. | Must have | 1. List all sessions of an event and sort them by level and duration ascendently by default.<br/> 2. Specify search terms.
6 | Create new session for an event | A user wants to create a new session for a particular event. | Must have | 
7 | Update existing session for an event | A user wants to update an existing session of a particular event. | Must have | 
8 | Delete session for an event | 1. A user wants to delete a particular session. <br/> 2. A user wants to delete all sessions for a particular event. | Must have | 
9 | Upvote / Downvote session | A user wants to upvote or downvote a particular session. | Must have | 

<br/>

## Notes
- The API should allow its users to select the fields they want to include in HTTP responses.