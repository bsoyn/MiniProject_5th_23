<template>
    <div>
        <Number
            label="ReaderId"
            v-model="value.readerId"
            :editMode="editMode"
        />
        <Number
            label="Point"
            v-model="value.point"
            :editMode="editMode"
        />
        <Number
            label="Cost"
            v-model="value.cost"
            :editMode="editMode"
        />
        <Boolean
            label="IsCompleted"
            v-model="value.isCompleted"
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
    name: 'Payment',
    mixins:[BaseEntity],
    components:{
    },
    
    data: () => ({
        path: "payments",
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
