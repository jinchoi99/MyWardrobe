Original App Design Project - README Template
===

# Closet/Wardrobe App

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Virtual closet where you can store and organize clothes, and save outfits, and get recommendations based on today's weather

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Clothes, Organizer, social networking
- **Mobile:** Camera used to post pic of clothes. Easy access to virtual closet, can prepare for what to wear whenever/wherever, instantly log clothes combination ideas
- **Story:** Allows users to add, organize, categorize, share clothes they have in their real closet, helps user keep track of what type of clothes and the location of where they had stored their clothes in real closet. Allows users to save clothes combination. Platform where they can share daily outfits with friends, give recommendations, exchange clothes.
- **Market:** Anyone (esp targeted for college students with limited space in their real closet, easy to lose clothes, buy too many clothes, etc)
- **Habit:** frequent usage, thinking of what to wear is essential part of daily routine
- **Scope:** keeping record of clothes -> simple, making it a social media platform -> complex

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* [fill in your required user stories here]
* [] User can log in/out
* [] User can sign up
* [] The current signed in user is persisted across app restarts
* [] User can take pictures/load pictures from gallery
* [] User can save pictures of their clothes into different categories
* [] User can add/delete categories
* [] User can add/delete/edit clothes
* [] User can select clothes and create a custom outfit combination
* [] User can view today's weather
* [] User can get recommendations for what to wear based on the weather
* [] User can view calendar
* [] User can log their outfits on specific days on the calendar
* [] User can click on the clothes to see more detail (description, location, price, etc)
* [] User can see the TOP 10 clothes they wear most often (ranking, sorting)
* [] User can add "favorites"

**Optional Nice-to-have Stories**

* [fill in your required user stories here]
* [] Pretty UI
* [] Delete background for clothes pictures
* [] AR/XR features
* [] AI recommendations
* [] Social media features (post, share, like, comment on others' closets/outfits)
* [] Marketplace (users can exchange/buy clothes from others)

### 2. Screen Archetypes
* Login / Register
  * User can log in/out
  * User can sign up
* Home
  * User can view categories
  * User can save pictures of their clothes into different categories
  * User can add/delete categories
  * User can view today's weather
  * User can get recommendations for what to wear based on the weather
* Category X (specific category)
  * User can add/delete/edit clothes
* Detail
  * User can click on the clothes to see more detail (description, location, price, etc)
* Outfit Creation
  * User can select clothes and create a custom outfit combination
* Calendar
  * User can view calendar
  * User can log their outfits on specific days on the calendar
* Favorites
  * User can add "favorites"
  * User can see the TOP 10 clothes they wear most often (ranking, sorting)

### 3. Navigation

**Tab Navigation** (Tab to Screen)
This is when the user switches between screens by simply clicking on a tab displayed at the top or bottom of the app.

* Home
* Calendar
* Favorites

**Flow Navigation** (Screen to Screen)
This is when the user taps on something on a screen and is taken to another screen. From that screen, they can go back to the previous screen, or navigate to another screen.

* Login / Register
  => Home
* Home
  => Category X
  => Calendar
  => Favorites
* Category X (specific category)
  => Detail
  => Home
* Detail
  => Category X
* Outfit Creation
* Calendar
  => Home
* Favorites
  => Home

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="wireframe.jpg" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
