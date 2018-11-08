$(document).ready(function() {
    var upload_url = "rest/speech/upload";
    var jobInProgress = false;
    var jobID;
    var timestamp_map; // Map of words to timestamp

    var uploadFile = function(file){
        var deferred = new $.Deferred();
        jobInProgress = true;

        $('#audio_status').removeClass('hidden');
        $('#audio_text').removeClass('hidden');
        $('#audio_text').text("Uploading audio file...");

        var formData = new FormData();
        formData.append('data', file);
        $.ajax({
            url: upload_url,
            type: "POST",
            data: formData,
            async: false,
            processData: false,
            success: function(response){
                // Insert the uploaded audio file in the audio portion.
                $('#audio').removeClass('hidden');
                if(response.id !== null){
                    // ID from the job comes back. Need to keep polling to determine when the job is done/failed.
                    deferred.resolve(response.id);
                } else {
                    deferred.reject();
                }
            },
            error: function(jqXHR){
                deferred.reject(jqXHR);
            }
        });
        // Automatically reject after 1 minute of no response
        setTimeout(function(){
            deferred.reject('timeout');
        },60000);
        return deferred;
    };

    var pollForTranscript = function() {

    };

    $('#file_browse').on('change', function(){        
        var audio = document.getElementById('audio');
        var reader = new FileReader();
        reader.onload = function(event) {
            audio.src = this.result;
            audio.controls = true;
        };
        reader.readAsDataURL(this.files[0]);
        uploadFile(this.files[0]).then(function(jobID){
            // Poll for updates using the token



            // Show pills for each word in the transcript of the audio file.
        }).fail(function(error){
            // Display error that no transcription was found.
        });
    });

    // Upon clicking the file upload icon, click the hidden file input to open up a finder.
    $('#file_upload').on('click', function(event){
        event.stopPropagation();
        $('#file_browse').click();
    });

    // Handle clicking on part of the transcript and jumping to the respective spot in the audio clip.
    $('#transcript').on('click', function(event){
        if($(event.target).is('span')){
            var span = event.target;
            var text = span.innerText;
            console.log("Clicked on '" + text + "' in the transcript.");

            var index = $(span).data('index');
            if(index){
                var time = timestamp_map[index];
                if(time){
                    $('#audio')[0].currentTime = time;
                }
            }  
        }                      
    });

    function debounce(func, wait, immediate) {
        var timeout;
        return function() {
            var context = this, args = arguments;
            var later = function() {
                timeout = null;
                if (!immediate) func.apply(context, args);
            };
            var callNow = immediate && !timeout;
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
            if (callNow) func.apply(context, args);
        };
    };

    var doSearch = debounce(function(search) {
        $.ajax({
            url: "rest/speech/search?phrase=" + search,
            type: "POST",
            success: function(response){
                console.log(response);
                if(response.id !== null){
                    // ID from the job comes back. Need to keep polling to determine when the job is done/failed.
                    console.log(response.id);
                } else {
                }
            },
            error: function(jqXHR){

            }
        });
    }, 500);

    // Filter down the transcript
    $('#transcript_search').on('input', function(){
        doSearch(this.value);
    });
});