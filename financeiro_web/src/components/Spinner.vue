<template>
    <div class="my-spinner">
        <!--
        <v-card
            color="primary"
            dark
        >
            <v-card-text>
                Please stand by
                <v-progress-linear
                    indeterminate
                    color="white"
                    class="mb-0"
                ></v-progress-linear>
            </v-card-text>
        </v-card>
        -->

        <v-progress-circular
            :size="120"
            indeterminate
        >
            Carregando
        </v-progress-circular>

    </div>
</template>

<script>
export default {
    name: 'Spinner'
}
</script>

<style scoped>
    .my-spinner {
        height: 100vh !important;
        width: 100vw !important;
        z-index: 99999999;
        position: absolute;
        
        background-color: lavender;
        color: darkblue;

        display: flex;
        justify-content: center;
        align-items: center;

        filter:alpha(opacity=50);
        opacity: 0.5;
        -moz-opacity:0.5;
        -webkit-opacity:0.8;
    }
</style>