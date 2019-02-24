package com.orobator.helloandroid.lesson4

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.orobator.helloandroid.lesson4.mvp.TipCalcPresenter
import com.orobator.helloandroid.lesson4.mvp.TipCalcPresenter.TipCalcPresentation
import org.junit.Test

class TipCalcPresenterTest {
  @Test
  fun `When tip greater than 100, error shown`() {
    val presenter = TipCalcPresenter()
    val presentation = mock<TipCalcPresentation> {
      on { checkAmount } doReturn "100"
      on { tipPercent } doReturn "9001"
    }

    presenter.attach(presentation)
    presenter.calculateTip()

    verify(presentation).getTipPercent()
    verify(presentation).getCheckAmount()

    verify(presentation).showError("Tip can't be greater than 100%")
    verifyNoMoreInteractions(presentation)
  }

  @Test
  fun `When tip calculated, tip calculation shown`() {
    val presenter = TipCalcPresenter()
    val presentation = mock<TipCalcPresentation> {
      on { checkAmount } doReturn "100"
      on { tipPercent } doReturn "20"
    }

    presenter.attach(presentation)
    presenter.calculateTip()

    verify(presentation).getTipPercent()
    verify(presentation).getCheckAmount()

    verify(presentation).setCheckAmount(100.0)
    verify(presentation).setTipPercent(20)
    verify(presentation).setTipAmount(20.0)
    verify(presentation).setGrandTotal(120.0)

    verifyNoMoreInteractions(presentation)
  }
}