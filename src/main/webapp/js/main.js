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

        var formData = new FormData(file);
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
        uploadFile(this.files[0]).then(function(token){
            // Show pills for each word in the transcript of the audio file.
            // Poll for updates using the token
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

    // Filter down the transcript
    $('#transcript_search').on('keydown', function(){
        var search = this.value;
        // Send POST to backend
        $.ajax({
            url: "rest/speech/health",
            type: "POST",
            data: search,
            async: true,
            success: function(response){
                if(response.id !== null){
                    // ID from the job comes back. Need to keep polling to determine when the job is done/failed.
                    deferred.resolve(response.id);
                } else {
                }
            },
            error: function(jqXHR){
            }
        });



        // var words = $('#transcript > span');
        // if(speech == ""){
        //     // Show all pills
        //     $('#transcript > span').show();
        // } else {
        //     // Filter down by the search criteria and if one matches instantly jump to the spot in the video.
        //     var spans = $('#transcript > span');

        // }
    });
});