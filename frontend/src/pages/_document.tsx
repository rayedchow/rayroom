import Navbar from '@/components/Navbar'
import { Html, Head, Main, NextScript } from 'next/document'

export default function Document() {
  return (
    <Html lang="en">
      <Head>
        <title>RayRoom</title>
      </Head>
      <body className="h-full">
        <Navbar />
        <main className="max-w-[60%] mx-auto h-full">
          <Main />
          <NextScript />
        </main>
      </body>
    </Html>
  )
}
