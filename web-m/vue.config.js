
module.exports = {
  publicPath: '/web/',
  chainWebpack: config => {
    config .plugin('html')
            .tap(args => {
                //就是 args[0]HtmlWebpackPlugin是配置
                args[0].title = '哈哈哈' //给你修改标题。
                args[0].favicon = './src/assets/logo.png' //可以给你添加logo
                args[0].minify = {
                      collapseWhitespace: true,
                      removeComments: true,
                      removeRedundantAttributes: true,
                      removeScriptTypeAttributes: true,
                      removeStyleLinkTypeAttributes: true,
                      useShortDoctype: true,
                      removeAttributeQuotes: false
                      }
              return args })

  }
}
