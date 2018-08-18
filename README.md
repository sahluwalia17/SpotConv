# SpotConv
Android application that reads a Spotify track URL through the Spotify API and returns a downloadable mp3 based off an seeking algorithm that is run alongside the YoutubeAPI.

![alt text](https://github.com/sahluwalia17/SpotConv/blob/master/images/auth.PNG)

Features include: 
- OkHttp to make network requests
- JSONObjects/JSONArrays -> Parsed to get certain information such as song, duration, and video scores
- AuthenticationRequest Builder to set up authorization for user access to Spotify/Youtube API's
- Threads -> Used to handle concurrent network requests
- Algorithim that parses Youtube videos and returns the most applicable video in accordance with the Spotify song provided
- Toast -> To provide user with popup information

![alt text](https://raw.githubusercontent.com/sahluwalia17/SpotConv/master/images/ui.PNG)

DOWNLOAD LINK: https://drive.google.com/file/d/1FPMNe_ivUrarmea-Kpk_nZarshp5zEBU/view?usp=sharing
