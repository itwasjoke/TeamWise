# TeamWise
Android application for task sharing
<p align="center">
<img src="https://github.com/itwasjoke/TeamWise-android/blob/main/screenshots/logo.png?raw=true" style="width: 30%">
</p>

## The theme of the app and the logic of work
TeamWise is an application for sharing tasks in a team between its members. Each of the participants has a specific position and tasks.
A team is usually a team that works within a task, project, or organization. Every team must have a leader.

All this is implemented in the TeamWise application. First of all, the leader is registered and creates a team. When creating, the name of the team, nickname, description are indicated, and lists of positions and types of tasks are also created.

<p align="center">
<img src="https://github.com/itwasjoke/TeamWise-android/blob/main/screenshots/screenshot6.jpg?raw=true" style="width: 30%">
</p>

The first thing you see is the main screen with various tasks. In the left menu, by selecting the "Team" item, you can go to a screen with a detailed description of your team. Here you can see the information that was created by the manager earlier. You can add new items to lists.

<p align="center">
<img src="https://github.com/itwasjoke/TeamWise-android/blob/main/screenshots/screenshot3.jpg?raw=true" style="width: 30%">
</p>

It remains to fill the team with participants. To do this, you can click on the copy invitation button, after which the invitation with the nickname of the team will be saved to the clipboard.
By clicking on the field with a member, you can see the biography of the person, as well as appoint him as an administrator.

The administrator, in turn, can see all the tasks, change the lists of the team. On the main screen with tasks, we see three tabs, each of which contains different tasks.

<p align="center">
<img src="https://github.com/itwasjoke/TeamWise-android/blob/main/screenshots/screenshot1.jpg?raw=true" style="width: 30%">
 </p>

The first will show those that were sent to you. Tasks submitted by you will be displayed in the second tab. When creating a new task, the user specifies a title, description, deadline, task type, and recipient. If you do not specify anyone in the recipient column, the task will go to the third tab as its own task. It is displayed only for you. Tasks in it are some kind of reminders.

<p align="center">
<img src="https://github.com/itwasjoke/TeamWise-android/blob/main/screenshots/screenshot2.jpg?raw=true" style="width: 30%">
</p>

Each task has a status indicator. It denotes the process of the participant's work with it.
Only three statuses
- Task waiting to be completed
- Task in progress
- Mission accomplished

In the settings, the user can change information about himself, the theme and language.

## Development

All code you can see in src folder. The Firebase service was used to create the application. I used it for distribution, database storage and user data.
