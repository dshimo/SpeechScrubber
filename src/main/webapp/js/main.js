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
        formData.append('filename', file.name);
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
                    // deferred.resolve(response.id);
                    deferred.resolve(result.id);
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

    var pollForTranscript = function(id, num_tries) {
        var deferred = $.Deferred();
        var done = false;
        if(num_tries > 0) {
            num_tries--;
            $.ajax({
                url: "rest/speech/check/" + id,
                type: "GET",
                data: formData,
                async: false,
                processData: false,
                success: function(response){
                    // Insert the uploaded audio file in the audio portion.                    
                    deferred.resolve(id);
                },
                error: function(jqXHR){
                    // deferred.reject(jqXHR);
                    if(num_tries == 0){
                        deferred.reject();
                    }
                    else {
                        return pollForTranscript(id, num_tries-1);
                    }                    
                }
            });       
        }
        return deferred;
    };

    var retrieveTranscript = function(id) {
        var deferred = $.deferred();
        $.ajax({
            url: "rest/speech/" + id + "/transcript",
            type: "GET",
            async: true,
            success: function(response){
                if(response.transcript !== null){
                    deferred.resolve(response.transcript);
                } else {
                    deferred.reject();
                }
            },
            error: function(jqXHR){
                deferred.reject(jqXHR);
            }
        });
        return deferred;
    };

    $('#file_browse').on('change', function(){
        var audio = document.getElementById('audio');
        var reader = new FileReader();
        reader.onload = function(event) {
            audio.src = this.result;
            audio.controls = true;
        };
        reader.readAsDataURL(this.files[0]);
        uploadFile(this.files[0]).then(function(id){
            // Poll for updates using the token
            pollForTranscript(id).then(function(id){
                // Retrieve the transcript
                retrieveTranscript(id).then(function(){

                }).fail(function(){

                });
            }).fail(function(){
                
            });
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