Original App Design Project - Brandon Zubia
===

# Find Work App

## Overview
### Description
App will connect users with service providers like babysitting, lawn care, plumbing, etc. Service providers will create their profile and users can search for what they need and will be matched with the best available service provider with respect to parameters(needs, location, cost).

### App Evaluation

- **Category:** Utility
- **Mobile:** mobile
- **Story:** Users can find service providers to complete a task
- **Market:** Everyone
- **Habit:** Users can search for service providers daily or less frequently depending on the task
- **Scope:** Platform to connect users with service providers

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Users can login/logout
* User will choose from two different flows, either looking for a service or providing a service
* Users will have a search feature to look for a service with filters for location
* Users can see a list of matched service providers
* Implement Google maps SDK into app to display available service providers with markers
* Build out scheduling feature 
* Swipe left to right to remove service request animation
* TODO: Find external library for visual polish


**Optional Nice-to-have Stories**

* Contractor listing displays serivce badges
* Users can chat with each other to better communicate about the task

### 2. Screen Archetypes

* Login Screen Screen 
    * User can login
* Sign Up Screen
    * User can sign up
* Choose role Screen
    * User will pick whether they are providing a service or looking for one
* Fill out profile screen
    * If the user is providing a serive they can add a description to add more detail to their work and add photos of previous work if appropriate
* Contractor feed
    * Users will see a list of users who are providing services in their area

* Search Screen
    * Users will type in what they are looking for with additional filters
* Contractor Profile Screen
    * User can tap on one of the listings and see more information about the user
* Chat Contacts Screen
    * Screen will show a list of past conversations
* User Chat Screen
    * Chat messages that the user clicked on

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Contractor feed
* Chat
* Profile

**Flow Navigation** (Screen to Screen)

* Sign Up Screen
   * => Choose Role Screen
* Choose Role Screen
    * => Fill out profile screen
* Fill out profile screen
    * => Contractor feed
* Login Screen
    * => Contractor feed
* Contractor feed
   * => Search Screen
   * => Contractor Profile Screen
* Contractor Profile Screen
    * => User Chat Screen
