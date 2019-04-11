<template>
<canvas class="comp-canvas-load" :width="wh" :height="wh" ref="cas">
        </canvas>
</template>
<script>
import {
    requestAnimationFrame,
    cancelAnimationFrame
} from '../../utils/tools'
export default {
    ctx: null,
    k: null,
    cycle: 0,
    props: {
        wh: {
            type: Number
        }
    },
    data() {
        return {
            deg: 0,
            isad: false
        }
    },
    mounted() {
        const vdeg = 0.03
        this.$options.ctx = this.$refs['cas'].getContext('2d')
        const am = () => {
            this.draw(this.deg)
            this.deg = this.deg + vdeg
            requestAnimationFrame(am)
        }
        this.$options.k = requestAnimationFrame(am)
    },
    destroyed() {
        cancelAnimationFrame(this.$options.k)
        this.$options.k = null
    },
    methods: {
        draw(deg) {
            let wh = this.wh
            this.$options.cycle++
                const {
                    $options: {
                        ctx
                    }
                } = this
            const endAngle = Math.PI * 1.5
            const times = parseInt(deg / endAngle)
            const k = parseInt(times / 2) + 1
            const startAngle = deg % endAngle
            ctx.clearRect(0, 0, wh, wh)
            ctx.save()
            ctx.translate(wh / 2, wh / 2)
            ctx.rotate(deg)
            ctx.beginPath()
            ctx.lineWidth = wh / 20
            ctx.strokeStyle = `rgb( 26 , 167 , 26)`
            if (times % 2 === 0) {
                ctx.arc(0, 0, wh / 4, startAngle + (k - 1) * endAngle, k * endAngle, false) // wu
            } else {
                ctx.arc(0, 0, wh / 4, k * endAngle, k * endAngle + startAngle, false) // you
            }
            ctx.stroke()
            ctx.closePath()
            ctx.restore()
        }
    }
}
</script>
<style>
.comp-canvas-load {
    display: inline-block;
}
</style>
