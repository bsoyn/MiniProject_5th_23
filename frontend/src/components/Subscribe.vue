<template>
    <div>
        <Number
            label="ReaderId"
            v-model="value.readerId"
            :editMode="editMode"
        />
        <Date
            label="SubscribeStartDate"
            v-model="value.subscribeStartDate"
            :editMode="editMode"
        />
        <Date
            label="SubscribeEndDate"
            v-model="value.subscribeEndDate"
            :editMode="editMode"
        />
        <v-row class="ma-0 pa-0">
            <v-spacer></v-spacer>
            <v-btn width="64px" color="primary" @click="save">
                저장
            </v-btn>
        </v-row>
    </div>
</template>


<script>
import BaseEntity from './base-ui/BaseEntity.vue'

export default {
    name: 'Subscribe',
    mixins:[BaseEntity],
    components:{
    },
    
    data: () => ({
        path: "subscribes",
        value: {
        }
    }),
    created(){
    },
    computed:{
    },
    methods: {
    },
}
</script>
