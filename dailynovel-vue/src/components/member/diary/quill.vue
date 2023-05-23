
<script setup>

import { ref,onMounted, onUpdated,defineProps } from 'vue';
import { Quill, QuillEditor } from '@vueup/vue-quill';
// import BlotFormatter from 'quill-blot-formatter'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import ImageUploader from 'quill-image-uploader';
// import axios from '../lib/axios';
import 'quill-image-uploader/dist/quill.imageUploader.min.css';

const quill = ref(null);
console.log(quill.value);
const props = defineProps({
    'content' : '',

});

//툴바 옵션들
const toolbarOptions = 
    [
        [{ 'header': 1 }, { 'header': 2 }],
        ['bold', 'italic', 'underline','image'],
        [{ 'list': 'ordered'}, { 'list': 'bullet' }],
        [{ 'font': [] }, ''],
        // [{ 'size': ['small', false, 'large', 'huge'] }],  // custom dropdown
        // [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
        // [{ 'color': [] }, { 'background': [] }],          // dropdown with defaults 
    ];


onMounted(() => {
    console.log("여기?");

    quill.value = new Quill('.editor-quill');
    console.log(quill.value);

    let contents = quill.value.getContents();
    // quill.value.on('text-change', () =>{
    //     console.log(quill.value.getContents());

    // });

    let refValue = quill.value;
    refValue.on('text-change',editortrigger);
    // refValue.on('text-change',geoFindMe);
});

onUpdated(() => {

    if(props.content != null)
      quill.value.setText(props.content);
    // console.log(quill.value);
});

function editortrigger (delta, oldDelta, source) {
    if (source == 'api') {
        console.log("An API call triggered this change.");
    } else if (source == 'user') {
        console.log("A user action triggered this change.");
    }
}



</script>

<template>


    <!-- <div
        class="editor-main-quill"> -->
        <!-- <p id="status"></p>
        <a id="map-link" target="_blank"></a> -->

        <!-- <div>{{ weatherData.weatherDesc }}</div> -->
        <!-- <div
            class="editor-quill"> -->
        <QuillEditor 
            class="editor-quill"
            placeholder="나의 마음을 찬찬히 넣어주세요"
            :toolbar="['bold', 'italic', 'underline', 'image']"
            />
            <!-- :modules="modules" -->



    
    <!-- </div> -->
    
</template>


<style scoped>


.ql-container{
    /* border: none; */

    /* width: 100%;
    height: 500px; */

    height: 80%;
    overflow-y: scroll;



}



</style>