import globalTip from "./globalTip"
const pulgins = [globalTip]
const regPulgins = (Vue) => {
	pulgins.map(p => Vue.use(p))
}
export default regPulgins
