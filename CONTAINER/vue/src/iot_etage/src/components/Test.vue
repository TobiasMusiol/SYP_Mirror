<template>
    <b-container>
        <b-row>
            <b-col cols="3" class="text-primary bg-warning" v-for="(test, index) in tests" v-bind:key="index">
                    {{test}}
            </b-col>
        </b-row>
        <hr>
        <div class="row">

            <div class="col-3 text-primary bg-warning" v-for="(test, index) in tests" v-bind:key="index">
                    {{test}}
            </div>
        </div>
    </b-container>
</template>

<script>
export default {
    name: 'Test',
    data: function(){
        return {
            tests: [
                'test 1',
                'test 2',
                'test 3',
                'test 4',
            ],
        };
    },
}
</script>