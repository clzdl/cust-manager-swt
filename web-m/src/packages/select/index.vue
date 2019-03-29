<template>
    <div class="ee-select-comp clear-fix" @click="handleClick">
        <i :class="k === value ? 'is-active' : ''" ></i><span><slot></slot></span>
    </div>
</template>
<script>
export default {
	props: {
		k: {
			type: [String, Number]
        },
        value: {
	        type: [String, Number]
        }
    },
    methods: {
		handleClick () {
			console.log(this.k)
			this.$emit('input', this.k)
        }
    }
}
</script>
<style lang="scss">
 .ee-select-comp {
     position: relative;
     display: inline-block;
     height: 60px;
     line-height: 60px;
     font-size: 28px;
     i, span {
         float: left;
     }
     i {
         position: relative;
         top: 50%;
         width: 30px;
         height: 30px;
         margin-right: 10px;
         border-radius: 100%;
         border: 1px solid #ccc;
         transform: translate(0, -50%);
         &.is-active {
             border: 0;
             background: url(./images/select.png) 0 0 no-repeat;
             background-size: 32px 32px;
             border: 0;
         }
     }
 }
</style>