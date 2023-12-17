# README

This is a demo app to showcase the use of multiple libraries.

## INSTALLATION
To use the app, you will need an Api Key from National Park Service.
It is free, you just need to fill a form to get one : https://www.nps.gov/subjects/developer/get-started.htm

To use your key : 
1. Create a file "keys.properties" at the root of the project
2. Add the following line : 
`NPS_API_KEY="your_key"`
3. Build the project, a file `BuildConfig` should have been generated with your key.

## OVERVIEW
From the home screen, you can navigate to :
- the list of US national parks, and then to the park info. 
- the list of the events by state.
- the app settings, allowing the user to customize how the parks data are fetched.
