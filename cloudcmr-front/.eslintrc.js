module.exports = {
    root: true,
    env: {
        node: true
    },
    'extends': [
        'plugin:vue/essential',
        '@vue/standard'
    ],
    rules: {
        'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
        'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
        indent: ['error', 4],
        // "vue/script-indent": ["error", 4, {
        //     "baseIndent": 0,
        //     "switchCase": 1,
        //     "ignores": []
        // }],
        "object-curly-spacing": ["warn", "always"],
        "space-before-function-paren": ["error", "never"],
    },
    parserOptions: {
        parser: 'babel-eslint',
        sourceType: 'module'
    },
    overrides: [
        {
            files: [
                '**/__tests__/*.{j,t}s?(x)'
            ],
            env: {
                mocha: true
            }
        },
        {
            files: ["*.vue"],
            rules: {
                "indent": "off"
            }
        },
        {
            files: [
                '**/__tests__/*.{j,t}s?(x)',
                '**/tests/unit/**/*.spec.{j,t}s?(x)'
            ],
            env: {
                jest: true
            }
        }
    ]
}
