<template>
    <div>
        <String
            label="Email"
            v-model="value.email"
            :editMode="editMode"
        />
        <String
            label="Name"
            v-model="value.name"
            :editMode="editMode"
        />
        <String
            label="Bio"
            v-model="value.bio"
            :editMode="editMode"
        />
        <String
            label="MajorWork"
            v-model="value.majorWork"
            :editMode="editMode"
        />
        <Boolean
            label="IsApproval"
            v-model="value.isApproval"
            :editMode="editMode"
        />
        <File
            offline
            label="portfolio"
            v-model="value.portfolio"
            :editMode="editMode"
            @change="change"
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
    name: 'Author',
    mixins:[BaseEntity],
    components:{
    },
    
    data: () => ({
        path: "authors",
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
