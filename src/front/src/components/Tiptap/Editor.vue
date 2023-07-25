<template>
    <el-tiptap :extensions="editor.extensions" :content="content" placeholder="Write something ..."/>
</template>

<script setup>
import {Document} from "@tiptap/extension-document";
import {History} from "@tiptap/extension-history";
import Paragraph from "@tiptap/extension-paragraph";
import {Text} from "@tiptap/extension-text";
import {Highlight} from "@tiptap/extension-highlight";
import {TaskList} from "@tiptap/extension-task-list";
import {TaskItem} from "@tiptap/extension-task-item";
import {StarterKit} from "@tiptap/starter-kit";
import {Image} from "@tiptap/extension-image"
import {Editor, EditorContent} from "@tiptap/vue-3";
import {onMounted, ref} from "vue";
import {Heading} from "@tiptap/extension-heading";
import {Bold} from "@tiptap/extension-bold";
import {Italic} from "@tiptap/extension-italic"
import {Strike} from "@tiptap/extension-strike";
import {Underline} from "@tiptap/extension-underline";
import {Link} from "@tiptap/extension-link";
import {Blockquote} from "@tiptap/extension-blockquote"
import {TextAlign} from "@tiptap/extension-text-align"
import {ListItem} from "@tiptap/extension-list-item"
import {BulletList} from "@tiptap/extension-bullet-list"
import {OrderedList} from "@tiptap/extension-ordered-list"
import {HardBreak} from "@tiptap/extension-hard-break"
import {HorizontalRule} from "@tiptap/extension-horizontal-rule"
import codemirror from "codemirror";
import "codemirror/lib/codemirror.css"; // import base style
import "codemirror/mode/xml/xml.js"; // language
import "codemirror/addon/selection/active-line.js"; // require active-line.js
import "codemirror/addon/edit/closetag.js";

const provider = ref(null)
const editor = ref(null)
const status = ref('connecting')
const content = ref("HI");

const options = {
    content: '',
    editable: true,
    supportImage: true,
    supportVideo: true
}
onMounted(() => {
    editor.value = new Editor({
        extensions: [
            StarterKit.configure({
                history: false,
            }),
            Document,
            Text,
            Paragraph,
            Heading({level:5}),
            Bold({ bubble: true }),
            Underline({ bubble: true }),
            Italic({ bubble: true }),
            Strike({ bubble: true }),
            Link({ bubble: true }),
            Image,
            Blockquote,
            TextAlign,
            ListItem,
            BulletList({ bubble: true }),
            OrderedList({ bubble: true }),
            Highlight,
            TaskList,
            TaskItem,
            History,
            HardBreak,
            HorizontalRule({ bubble: true }),
            History
        ],
        content: "HI",
    });
});

</script>
