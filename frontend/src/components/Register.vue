<template>

    <v-card outlined>
        <v-card-title>
            Register
        </v-card-title>

        <v-card-text>
            <String label="Email" v-model="value.email" :editMode="editMode"/>
            <String label="Name" v-model="value.name" :editMode="editMode"/>
            <String label="Bio" v-model="value.bio" :editMode="editMode"/>
            <String label="MajorWork" v-model="value.majorWork" :editMode="editMode"/>
            <File offline label="Portfolio" v-model="value.portfolio" :editMode="editMode" @change="change"/>
            <Boolean label="IsApproval" v-model="value.isApproval" :editMode="editMode"/>
            <Number label="Id" v-model="value.id" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="register"
            >
                Register
            </v-btn>
            
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="close"
            >
                Close
            </v-btn>
        </v-card-actions>
    </v-card>

</template>

<script>

export default {
    name: 'RegisterCommand',
    components:{
    },
    props: {},
    data: () => ({
        editMode: true,
        value: {},
    }),
    created() {
        this.value.email = '';
        this.value.name = '';
        this.value.bio = '';
        this.value.majorWork = '';
        this.value.portfolio = {};
        this.value.isApproval = false;
        this.value.id = 0;
    },
    watch: {
    },
    methods: {
        register() {
            this.$emit('register', this.value);
        },
        close() {
            this.$emit('closeDialog');
        },
        change() {
            this.$emit("update:modelValue", this.value);
        },
    }
}
</script>

