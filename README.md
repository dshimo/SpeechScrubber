# SpeechScrubber
Speech Scrubber is a handy web application to transcribe audio files and to see a transcript displayed that lets you click on the transcript to jump to the respective time in the audio clip. We utilize the Rev.AI speech to text API to transcribe audio files then provide extra functionality to jump to the relevant part of the audio file that you want.

To run the app locally, clone the github repository and run ./gradlew installApps to build the war file and then ./gradlew libertyStart to start the Liberty server. Proceed to http://localhost:9080/speechScrubber/ in your browser to view the web application. Upload an audio file and let it process the transcript, then you will be able to click on words in the audio clip to jump to certain timestamps or search for words in the audio clip to also jump to that spot in the audio.

Tools used: IBM's Open Liberty web application server, gradle, jQuery, and bootstrap.
