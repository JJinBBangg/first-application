<template>
    <div>
        <div class="p-10">
            <div
                    class="editor"
                    v-if="editor"
            >
                <menu-bar
                    class="editor__header"
                    :editor="editor"
                />
                <editor-content
                    class="editor__content"
                    :editor="editor"
                />
            </div>
        </div>
    </div>
    <el-button @click="json">입력</el-button>
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
import {Editor, EditorContent} from "@tiptap/vue-3";
import MenuBar from "@/components/MenuBar.vue";
import {onMounted, ref} from "vue";

const provider= ref(null)
const editor= ref(null)
const status= ref('connecting')
const content= ref("HI");

onMounted(() => {
    editor.value = new Editor({
        extensions: [
            StarterKit.configure({
                history: false,
            }),
            Highlight,
            TaskList,
            TaskItem,
            Document,
            Paragraph,
            Text,
            History,
        ],
        content: "HI",
    });
});
const json = () => {
    console.log(editor.value.getHTML())
}


</script>
<style lang="scss">
.editor {
    background-color: #C4FFDF;
    border: 3px solid #0D0D0D;
    border-radius: 0.75rem;
    color: #0D0D0D;
    display: flex;
    flex-direction: column;
    max-height: 26rem;


    &__header {
        align-items: center;
        background: #143E1C;
        border-bottom: 3px solid #143E1C;
        border-top-left-radius: 0.25rem;
        border-top-right-radius: 0.25rem;
        display: flex;
        flex: 0 0 auto;
        flex-wrap: wrap;
        padding: 0.25rem;
    }

    &__content {
        flex: 1 1 auto;
        overflow-x: hidden;
        overflow-y: auto;
        padding: 1.25rem 1rem;
        -webkit-overflow-scrolling: touch;

        /* 스크롤바 스타일 지정 */
        scrollbar-width: thin;
        scrollbar-color: rgba(13, 13, 13, 0.6) transparent;

        &::-webkit-scrollbar {
            width: 8px;
        }

        &::-webkit-scrollbar-track {
            background-color: transparent;
        }

        &::-webkit-scrollbar-thumb {
            background-color: rgba(13, 13, 13, 0.6);
            border-radius: 4px;
        }

    }


}

/* Basic editor styles */
.ProseMirror {
    > * + * {
        margin-top: 0.75em;
    }
    &:focus {
        outline: none;
    }

    ul,
    ol {
        padding: 0 1rem;
    }

    h1,
    h2,
    h3,
    h4,
    h5,
    h6 {
        line-height: 1.1;
    }

    code {
        background-color: rgba(#616161, 0.1);
        color: #616161;
    }

    pre {
        background: #0D0D0D;
        border-radius: 0.5rem;
        color: #FFF;
        font-family: 'JetBrainsMono', monospace;
        padding: 0.75rem 1rem;

        code {
            background: none;
            color: inherit;
            font-size: 0.8rem;
            padding: 0;
        }
    }

    mark {
        background-color: #FAF594;
    }

    img {
        height: auto;
        max-width: 100%;
    }

    hr {
        margin: 1rem 0;
    }

    blockquote {
        border-left: 2px solid rgba(#0D0D0D, 0.1);
        padding-left: 1rem;
    }

    hr {
        border: none;
        border-top: 2px solid rgba(#0D0D0D, 0.1);
        margin: 2rem 0;
    }

    ul[data-type="taskList"] {
        list-style: none;
        padding: 0;

        li {
            align-items: center;
            display: flex;

            > label {
                flex: 0 0 auto;
                margin-right: 0.5rem;
                user-select: none;
            }

            > div {
                flex: 1 1 auto;
            }
        }
    }

}
</style>
<!--<style lang="scss">-->
<!--.editor {-->
<!--    background-color: #C4FFDF;-->
<!--    border: 3px solid #143E1C;-->
<!--    border-radius: 0.4rem;-->
<!--    color: black;-->
<!--    display: flex;-->
<!--    flex-direction: column;-->
<!--    max-height: 26rem;-->

<!--    &__header {-->
<!--        align-items: center;-->
<!--        background: #143E1C;-->
<!--        border-bottom: 3px solid #0d0d0d;-->
<!--        border-top-left-radius: 0.25rem;-->
<!--        border-top-right-radius: 0.25rem;-->
<!--        display: flex;-->
<!--        flex: 0 0 auto;-->
<!--        flex-wrap: wrap;-->
<!--        padding: 0.25rem;-->
<!--    }-->

<!--    &__content {-->
<!--        flex: 1 1 auto;-->
<!--        overflow-x: hidden;-->
<!--        overflow-y: auto;-->
<!--        padding: 1.25rem 1rem;-->
<!--        -webkit-overflow-scrolling: touch;-->
<!--    }-->

<!--    &__footer {-->
<!--        align-items: center;-->
<!--        border-top: 3px solid #0D0D0D;-->
<!--        color: #0D0D0D;-->
<!--        display: flex;-->
<!--        flex: 0 0 auto;-->
<!--        flex-wrap: wrap;-->
<!--        font-size: 12px;-->
<!--        font-weight: 600;-->
<!--        justify-content: space-between;-->
<!--        padding: 0.25rem 0.75rem;-->
<!--        white-space: nowrap;-->
<!--    }-->

<!--    /* Some information about the status */-->
<!--    &__status {-->
<!--        align-items: center;-->
<!--        border-radius: 5px;-->
<!--        display: flex;-->

<!--        &::before {-->
<!--            background: rgba(#0D0D0D, 0.5);-->
<!--            border-radius: 50%;-->
<!--            content: ' ';-->
<!--            display: inline-block;-->
<!--            flex: 0 0 auto;-->
<!--            height: 0.5rem;-->
<!--            margin-right: 0.5rem;-->
<!--            width: 0.5rem;-->
<!--        }-->

<!--        &&#45;&#45;connecting::before {-->
<!--            background: #616161;-->
<!--        }-->

<!--        &&#45;&#45;connected::before {-->
<!--            background: #B9F18D;-->
<!--        }-->
<!--    }-->

<!--    &__name {-->
<!--        button {-->
<!--            background: none;-->
<!--            border: none;-->
<!--            border-radius: 0.4rem;-->
<!--            color: #0D0D0D;-->
<!--            font: inherit;-->
<!--            font-size: 12px;-->
<!--            font-weight: 600;-->
<!--            padding: 0.25rem 0.5rem;-->

<!--            &:hover {-->
<!--                background-color: #0D0D0D;-->
<!--                color: #FFF;-->
<!--            }-->
<!--        }-->
<!--    }-->
<!--}-->



<!--/* Basic editor styles */-->
<!--.ProseMirror {-->

<!--    > * + * {-->
<!--        margin-top: 0.75em;-->
<!--    }-->

<!--    ul,-->
<!--    ol {-->
<!--        padding: 0 1rem;-->
<!--    }-->

<!--    h1,-->
<!--    h2,-->
<!--    h3,-->
<!--    h4,-->
<!--    h5,-->
<!--    h6 {-->
<!--        line-height: 1.1;-->
<!--    }-->

<!--    code {-->
<!--        background-color: rgba(#616161, 0.1);-->
<!--        color: #616161;-->
<!--    }-->

<!--    pre {-->
<!--        background: #0D0D0D;-->
<!--        border-radius: 0.5rem;-->
<!--        color: #FFF;-->
<!--        font-family: 'JetBrainsMono', monospace;-->
<!--        padding: 0.75rem 1rem;-->

<!--        code {-->
<!--            background: none;-->
<!--            color: inherit;-->
<!--            font-size: 0.8rem;-->
<!--            padding: 0;-->
<!--        }-->
<!--    }-->

<!--    mark {-->
<!--        background-color: #FAF594;-->
<!--    }-->

<!--    img {-->
<!--        height: auto;-->
<!--        max-width: 100%;-->
<!--    }-->

<!--    hr {-->
<!--        margin: 1rem 0;-->
<!--    }-->

<!--    blockquote {-->
<!--        border-left: 2px solid rgba(#0D0D0D, 0.1);-->
<!--        padding-left: 1rem;-->
<!--    }-->

<!--    hr {-->
<!--        border: none;-->
<!--        border-top: 2px solid rgba(#0D0D0D, 0.1);-->
<!--        margin: 2rem 0;-->
<!--    }-->

<!--    ul[data-type="taskList"] {-->
<!--        list-style: none;-->
<!--        padding: 0;-->

<!--        li {-->
<!--            align-items: center;-->
<!--            display: flex;-->

<!--            > label {-->
<!--                flex: 0 0 auto;-->
<!--                margin-right: 0.5rem;-->
<!--                user-select: none;-->
<!--            }-->

<!--            > div {-->
<!--                flex: 1 1 auto;-->
<!--            }-->
<!--        }-->
<!--    }-->
<!--}-->
<!--</style>-->