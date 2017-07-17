const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = (env = {}) => {
    const isProd = env == 'production';
    const publicFolder = path.join(__dirname, '../src/main/resources/static/');

    return {
        entry: {
            app: './src/bootstrap.js'
        },
        output: {
            path: publicFolder,
            publicPath: '/',
            filename: '[name].js'
        },
        module: {
            rules: [
                {
                    test: /\.js$/,
                    exclude: /(node_modules)/,
                    use: {
                        loader: 'babel-loader',
                        options: {
                            presets: [
                                ['es2015'],
                                ['react-app'],
                                ['react']
                            ]
                        }
                    }
                },
                {
                    test: /\.css$/,
                    use: ExtractTextPlugin.extract({
                        fallback: [{
                            loader: 'style-loader',
                        }],
                        use: [{
                            loader: 'css-loader',
                        }]
                    })
                },
                {
                    test: /\.scss$/,
                    use: ExtractTextPlugin.extract({
                        fallback: [{
                            loader: 'style-loader',
                        }],
                        use: [{
                            loader: 'css-loader',
                        }, {
                            loader: 'sass-loader',
                            options: {
                                sourceMap: true
                            }
                        }]
                    })
                },
                {
                    test: /\.(png|jpg)$/,
                    use: 'url?limit=8192'
                }
            ]
        },
        plugins: [
            new webpack.DefinePlugin({
                'process.env.NODE_ENV': JSON.stringify(process.env.NODE_ENV || 'development'),
                VERSION: JSON.stringify(require('./package.json').version)
            }),
            new ExtractTextPlugin('[name].css', {
                allChunks: true,
                disable: !isProd
            }),
            new HtmlWebpackPlugin({
                template: './index.html',
                filename: 'index.html',
                inject: 'body'
            })
        ].concat(isProd ? [
            new webpack.NoEmitOnErrorsPlugin(),
            new webpack.optimize.UglifyJsPlugin(),
            new CopyWebpackPlugin([{
                from: '*.svg'
            }]),
        ] : []),
        devServer: {
            port: 9000
        }
    };
};
